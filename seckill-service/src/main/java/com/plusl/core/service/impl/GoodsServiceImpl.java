package com.plusl.core.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.plusl.core.service.GoodsService;
import com.plusl.core.service.mapper.GoodsMapper;
import com.plusl.framework.common.convert.goods.GoodsMapStruct;
import com.plusl.framework.common.dataobject.GoodsDO;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.SeckillGoods;
import com.plusl.framework.common.redis.RedisKeyUtils;
import com.plusl.framework.common.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.plusl.framework.common.redis.RedisConstant.NEVER_EXPIRE;

/**
 * @program: seckill-parent
 * @description: 普通商品业务处理
 * @author: PlusL
 * @create: 2022-07-07 11:40
 **/
@Slf4j
@Service
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
    public GoodsDTO getGoodsDoByGoodsId(Long goodsId) {
        String goodsRedisKey = RedisKeyUtils.getSeckillGoodsPrefix(goodsId);
        GoodsDTO goodsDTOByCache = redisUtil.get(goodsRedisKey, GoodsDTO.class);
        if (ObjectUtil.isEmpty(goodsDTOByCache)) {
            // 缓存中未命中，查数据库并将商品对象加入缓存
            GoodsDTO goodsDTOByDB = GoodsMapStruct.INSTANCE.convert(goodsMapper.getGoodsDoByGoodsId(goodsId));
            redisUtil.set(goodsRedisKey, goodsDTOByDB, NEVER_EXPIRE);
            return goodsDTOByDB;
        }
        return goodsDTOByCache;
    }

    @Override
    public Boolean reduceOneStock(SeckillGoods seckillGoods) {
        int ret = goodsMapper.reduceOneStock(seckillGoods);
        log.info("DB减库存 秒杀商品信息 : {} 返回结果 : {}", JSON.toJSONString(seckillGoods), ret);
        return ret > 0;
    }

    @Override
    public Boolean initSetGoodsMock(GoodsDTO goodsDTO) {
        return redisUtil.set(RedisKeyUtils.getSeckillGoodsStockPrefix(goodsDTO.getId())
                , goodsDTO.getStockCount(), NEVER_EXPIRE);
    }

    @Override
    public Boolean delStockCountCache(Long goodsId) {
        Boolean isSuccess = redisUtil.delete(RedisKeyUtils.getSeckillGoodsStockPrefix(goodsId));
        log.info("删除商品ID:[{}] 缓存", goodsId);
        return isSuccess;
    }

}
