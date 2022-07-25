package com.plusl.core.service.impl;


import com.plusl.core.service.GoodsService;
import com.plusl.core.service.mapper.GoodsMapper;
import com.plusl.framework.common.convert.goods.GoodsMapStruct;
import com.plusl.framework.common.dataobject.GoodsDO;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.SeckillGoods;
import com.plusl.framework.common.redis.GoodsKey;
import com.plusl.framework.common.redis.RedisUtil;
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
        return GoodsMapStruct.INSTANCE.convert(goodsMapper.getGoodsDoByGoodsId(goodsId));
    }

    @Override
    public Boolean reduceStock(Long goodsId) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goodsId);
        int ret = goodsMapper.reduceStock(g);
        //TODO: 此处返回类型可能与包装类冲突
        return ret > 0;
    }

    @Override
    public Boolean initSetGoodsMock(GoodsDTO goodsDTO) {
        return redisUtil.set(GoodsKey.getSeckillGoodsStock, "" + goodsDTO.getId(), goodsDTO.getStockCount());
    }

}
