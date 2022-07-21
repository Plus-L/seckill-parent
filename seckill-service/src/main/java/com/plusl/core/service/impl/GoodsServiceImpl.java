package com.plusl.core.service.impl;


import com.plusl.core.service.Interface.GoodsService;
import com.plusl.core.service.mapper.GoodsMapper;
import com.plusl.framework.common.convert.goods.GoodsMapStruct;
import com.plusl.framework.common.dataobject.GoodsDO;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.SeckillGoods;
import com.plusl.framework.common.enums.result.Result;
import com.plusl.framework.common.enums.status.ResultStatus;
import com.plusl.framework.common.redis.GoodsKey;
import com.plusl.framework.common.redis.RedisUtil;
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
    RedisUtil redisUtil;

    @Override
    public List<GoodsDTO> listGoodsDTO() {
        List<GoodsDO> goodsDOList = goodsMapper.listGoodsDo();
        return GoodsMapStruct.INSTANCE.convertListDOtoDTO(goodsDOList);
    }

    @Override
    public GoodsDTO getGoodsDoByGoodsId(long goodsId) {
        return GoodsMapStruct.INSTANCE.convert(goodsMapper.getGoodsDoByGoodsId(goodsId));
    }

    @Override
    public boolean reduceStock(GoodsDTO goodsDTO) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goodsDTO.getId());
        int ret = goodsMapper.reduceStock(g);
        return ret > 0;
    }

    @Override
    public Result<GoodsDTO> initSetGoodsMock(GoodsDTO goodsDTO) {
        Result<GoodsDTO> result = Result.build();
        boolean ok = redisUtil.set(GoodsKey.getSeckillGoodsStock, "" + goodsDTO.getId(), goodsDTO.getStockCount());
        if (!ok) {
            result.withError(ResultStatus.REDIS_ERROR);
        } else {
            result.setData(goodsDTO);
        }
        return result;
    }

}
