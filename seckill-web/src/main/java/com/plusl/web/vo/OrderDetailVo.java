package com.plusl.web.vo;

import com.plusl.framework.common.entity.OrderInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: seckill-parent
 * @description: 订单详情VO
 * @author: PlusL
 * @create: 2022-07-08 11:40
 **/
@Getter
@Setter
public class OrderDetailVo implements Serializable {
    public GoodsVo goods;
    public OrderInfo order;

}
