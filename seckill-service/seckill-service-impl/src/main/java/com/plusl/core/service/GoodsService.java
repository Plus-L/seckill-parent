package com.plusl.core.service;

import com.plusl.core.facade.api.entity.SeckillGoods;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;


import java.util.List;

/**
 * @program: seckill-parent
 * @description: 商品服务接口
 * @author: PlusL
 * @create: 2022-07-11 10:47
 **/
public interface GoodsService {

    /**
     * 获取商品列表
     *
     * @return 商品列表的list
     */
    List<GoodsDTO> listGoodsDTO();

    /**
     * 通过商品ID获取商品信息
     *
     * @param goodsId 商品ID
     * @return 商品Vo实体
     */
    GoodsDTO getGoodsDtoByGoodsId(Long goodsId);

    /**
     * 减少库存
     *
     * @param seckillGoods 商品实体dto
     * @return 是否成功
     */
    Boolean reduceOneStock(SeckillGoods seckillGoods);

    /**
     * 初始化设置商品库存到缓存中，方便进行后续预减库存操作
     *
     * @param goodsDTO 商品dtp
     * @return 布尔
     */
    Boolean initSetGoodsMock(GoodsDTO goodsDTO);

    /**
     * 删除库存缓存
     *
     * @param goodsId 商品ID
     * @return 布尔值
     */
    Boolean delStockCountCache(Long goodsId);

}
