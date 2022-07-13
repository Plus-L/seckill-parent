package com.plusl.service.impl;

import com.plusl.common.entity.SeckillGoods;
import com.plusl.common.enums.result.Result;
import com.plusl.common.enums.status.ResultStatus;
import com.plusl.common.vo.GoodsVo;
import com.plusl.service.GoodsService;
import com.plusl.service.mapper.GoodsMapper;
import com.plusl.service.redis.GoodsKey;
import com.plusl.service.redis.RedisService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: seckill-parent
 * @description: 普通商品业务处理
 * @author: PlusL
 * @create: 2022-07-07 11:40
 **/
@Service
@DubboService(interfaceClass = GoodsService.class)
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    RedisService redisService;

    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    @Override
    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsMapper.reduceStock(g);
        return ret > 0;
    }

    @Override
    public Result<GoodsVo> initSetGoodsMock(GoodsVo goodsVo) {
        Result<GoodsVo> result = Result.build();
        boolean ok = redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
        if (!ok) {
            result.withError(ResultStatus.REDIS_ERROR);
        } else {
            result.setData(goodsVo);
        }
        return result;
    }


}
