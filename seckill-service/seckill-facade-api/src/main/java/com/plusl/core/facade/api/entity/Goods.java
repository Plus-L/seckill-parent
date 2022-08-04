package com.plusl.core.facade.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "商品ID不能为空")
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
    @NotNull(message = "商品价格不能为空")
    private Double goodsPrice;

    /**
     * 商品库存
     */
    @NotNull(message = "商品库存不能为空")
    private Integer goodsStock;

}
