package com.plusl.service;

import com.plusl.common.enums.result.Result;
import com.plusl.common.vo.GoodsVo;

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
    public List<GoodsVo> listGoodsVo();

    /**
     * 通过商品ID获取商品信息
     *
     * @param goodsId 商品ID
     * @return 商品Vo实体
     */
    public GoodsVo getGoodsVoByGoodsId(long goodsId);

    /**
     * 减少库存
     *
     * @param goods 商品实体
     * @return 是否成功
     */
    public boolean reduceStock(GoodsVo goods);

    Result<GoodsVo> initSetGoodsMock(GoodsVo goodsVo);

}
