package com.plusl.core.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.plusl.core.facade.api.entity.SeckillGoods;
import com.plusl.core.service.GoodsService;
import com.plusl.core.service.convert.goods.GoodsMapStruct;
import com.plusl.core.service.mapper.GoodsMapper;
import com.plusl.core.service.dataobject.GoodsDO;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.framework.redis.RedisKeyUtils;
import com.plusl.framework.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.plusl.framework.redis.constant.RedisConstant.NEVER_EXPIRE;


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
    RedisService redisService;

    @Override
    public List<GoodsDTO> listGoodsDTO() {
        List<GoodsDO> goodsDOList = goodsMapper.listGoodsDo();
        return GoodsMapStruct.INSTANCE.convertListDOtoDTO(goodsDOList);
    }

    @Override
    public GoodsDTO getGoodsDtoByGoodsId(Long goodsId) {
        // 商品实体key
        String goodsRedisKey = RedisKeyUtils.getSeckillGoodsPrefix(goodsId);
        // 商品库存key
        String stockRedisKey = RedisKeyUtils.getSeckillGoodsStockPrefix(goodsId);

        GoodsDTO goodsDTOByCache = redisService.get(goodsRedisKey, GoodsDTO.class);

        if (ObjectUtil.isEmpty(goodsDTOByCache)) {
            // 缓存中未命中，查数据库并将商品对象加入缓存
            GoodsDTO goodsDTOByDB = GoodsMapStruct.INSTANCE.convert(goodsMapper.getGoodsDoByGoodsId(goodsId));
            redisService.set(goodsRedisKey, goodsDTOByDB, NEVER_EXPIRE);
            redisService.set(stockRedisKey, goodsDTOByDB.getStockCount(), NEVER_EXPIRE);
            return goodsDTOByDB;
        }
        // 获取商品时将该商品的当前最新库存给缓存进去
        redisService.setnx(stockRedisKey, JSON.toJSONString(goodsDTOByCache.getStockCount()));

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
        return redisService.set(RedisKeyUtils.getSeckillGoodsStockPrefix(goodsDTO.getGoodsId())
                , goodsDTO.getStockCount(), NEVER_EXPIRE);
    }

    /**
     * 将商品实体的缓存以及商品库存的缓存都删除
     *
     * @param goodsId 商品ID
     * @return 布尔值
     */
    @Override
    public Boolean delStockCountCache(Long goodsId) {

        Boolean delGoodsIsSuccess = redisService.delete(RedisKeyUtils.getSeckillGoodsStockPrefix(goodsId));
        Boolean delStockIsSuccess = redisService.delete(RedisKeyUtils.getSeckillGoodsPrefix(goodsId));
        log.info("删除商品ID:[{}] 实体缓存以及库存缓存", goodsId);
        return delStockIsSuccess && delGoodsIsSuccess;
    }

}
