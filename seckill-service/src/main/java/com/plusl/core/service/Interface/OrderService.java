package com.plusl.core.service.Interface;

import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;

/**
 * @program: seckill-parent
 * @description: 订单服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:12
 **/
public interface OrderService {

    OrderInfo getOrderById(long id);

    SeckillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId);

    OrderInfo createOrder(User user, GoodsDTO goods);


}
