package com.plusl.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

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
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
