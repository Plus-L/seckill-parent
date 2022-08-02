package com.plusl.framework.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: seckill-parent
 * @description: GoodsEntity
 * @author: PlusL
 * @create: 2022-07-05 14:51
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品标签
     */
    private String goodsTitle;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品细节
     */
    private String goodsDetail;

    /**
     * 商品价格
     */
    private Double goodsPrice;

    /**
     * 商品库存
     */
    private Integer goodsStock;

}
