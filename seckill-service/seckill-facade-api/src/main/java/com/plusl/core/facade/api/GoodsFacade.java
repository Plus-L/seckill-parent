package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.dto.GoodsDTO;

import java.util.List;

/**
 * @program: seckill-framework
 * @description: 商品Facade
 * @author: PlusL
 * @create: 2022-07-25 09:30
 **/
public interface GoodsFacade {

    /**
     * 获取商品列表
     *
     * @return 规范化FacadeResult商品列表
     */
    FacadeResult<List<GoodsDTO>> getGoodsDTOList();

    /**
     * 通过商品ID获取商品信息
     *
     * @param goodsId 商品ID
     * @return 商品dto
     */
    FacadeResult<GoodsDTO> getGoodsDtoByGoodsId(Long goodsId);

    /**
     * 通过商品ID减库存
     *
     * @param goodsId 商品ID
     * @return 布尔值-是否成功
     */
    FacadeResult<Boolean> reduceStockByGoodsId(Long goodsId);

    /**
     * 初始化将库存加载到缓存中
     *
     * @param goodsDTO 商品dto
     * @return 布尔值-是否成功
     */
    FacadeResult<Boolean> initSetGoodsMock(GoodsDTO goodsDTO);

}
