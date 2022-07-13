package com.plusl.common.vo;

import com.plusl.common.entity.OrderInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: seckill-parent
 * @description: 订单详情VO
 * @author: PlusL
 * @create: 2022-07-08 11:40
 **/
@Getter
@Setter
public class OrderDetailVo {
    public GoodsVo goods;
    public OrderInfo order;

}
