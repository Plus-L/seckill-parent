package com.plusl.core.service.Interface;

import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.enums.result.Result;

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
    GoodsDTO getGoodsDoByGoodsId(long goodsId);

    /**
     * 减少库存
     *
     * @param goodsDTO 商品实体dto
     * @return 是否成功
     */
    boolean reduceStock(GoodsDTO goodsDTO);

    Result<GoodsDTO> initSetGoodsMock(GoodsDTO goodsDTO);

}
