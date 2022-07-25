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

    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;

}
