package com.plusl.core.service.mapper;

import com.plusl.framework.common.entity.SeckillGoods;
import com.plusl.framework.common.dataobject.GoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LJH
 */
@Mapper
public interface GoodsMapper {

    /**
     * 查询商品信息列表
     *
     * @return 商品信息列表
     */
    List<GoodsDO> listGoodsDo();

    /**
     * 通过ID获取商品信息
     *
     * @param goodsId 商品ID
     * @return 商品对象
     */
    GoodsDO getGoodsDoByGoodsId(@Param("goodsId") long goodsId);

    /**
     * 减少库存
     *
     * @param seckillGoods 消杀商品
     * @return 减少后的库存
     */
    int reduceStock(SeckillGoods seckillGoods);

}
