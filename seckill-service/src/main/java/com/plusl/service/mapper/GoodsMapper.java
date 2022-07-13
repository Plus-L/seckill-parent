package com.plusl.service.mapper;

import com.plusl.common.entity.SeckillGoods;
import com.plusl.common.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
    public List<GoodsVo> listGoodsVo();

    /**
     * 通过ID获取商品信息
     *
     * @param goodsId 商品ID
     * @return 商品对象
     */
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    /**
     * 减少库存
     *
     * @param seckillGoods 消杀商品
     * @return 减少后的库存
     */
    public int reduceStock(SeckillGoods seckillGoods);

}
