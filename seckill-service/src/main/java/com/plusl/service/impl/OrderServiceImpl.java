package com.plusl.service.impl;

import com.plusl.common.entity.OrderInfo;
import com.plusl.common.entity.SeckillOrder;
import com.plusl.common.entity.User;
import com.plusl.common.vo.GoodsVo;
import com.plusl.service.OrderService;
import com.plusl.service.mapper.OrderMapper;
import com.plusl.service.redis.OrderKey;
import com.plusl.service.redis.RedisService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: seckill-parent
 * @description: 订单服务
 * @author: PlusL
 * @create: 2022-07-07 15:23
 **/
@Service
@DubboService(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

    @Override
    public OrderInfo getOrderById(long id) {
        return orderMapper.getOrderById(id);
    }

    @Override
    public SeckillOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        return orderMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    @Override
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());

        long orderId = orderMapper.insert(orderInfo);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        orderMapper.insertMiaoshaOrder(seckillOrder);

        redisService.set(OrderKey.getMiaoshaOrderByUidGid, "" + user.getNickname() + "_" + goods.getId(), seckillOrder);

        return orderInfo;
    }

}
