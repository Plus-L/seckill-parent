package com.plusl.service;

import com.plusl.common.entity.OrderInfo;
import com.plusl.common.entity.SeckillOrder;
import com.plusl.common.entity.User;
import com.plusl.common.vo.GoodsVo;

/**
 * @program: seckill-parent
 * @description: 订单服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:12
 **/
public interface OrderService {

    public OrderInfo getOrderById(long id);

    public SeckillOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);

    public OrderInfo createOrder(User user, GoodsVo goods);


}
