package com.plusl.framework.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: seckill-parent
 * @description:
 * @author: PlusL
 * @create: 2022-07-07 15:27
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Alias("OrderInfo")
public class OrderInfo implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 地址
     */
    private Long deliveryAddrId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 商品价格
     */
    private Double goodsPrice;

    /**
     * 订单渠道
     */
    private Integer orderChannel;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建（订单）时间
     */
    private Date createDate;

    /**
     * 支付时间
     */
    private Date payDate;
}
