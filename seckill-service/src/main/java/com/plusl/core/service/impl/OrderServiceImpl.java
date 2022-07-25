package com.plusl.core.service.impl;

import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.redis.OrderKey;
import com.plusl.framework.common.redis.RedisUtil;
import com.plusl.core.service.OrderService;
import com.plusl.core.service.mapper.OrderMapper;
import com.plusl.core.service.util.SnowflakeIdWorker;
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
    RedisUtil redisUtil;

    @Override
    public OrderInfo getOrderById(Long orderId) {
        return orderMapper.getOrderById(orderId);
    }

    @Override
    public SeckillOrder  getSeckillOrderByUserIdGoodsId(Long userId, Long goodsId) {
        //TODO：直接取数据库可能导致服务崩掉的问题，解决方法如加到缓存，加入到缓存还有缓存击穿的问题需要考虑
        return orderMapper.getSeckillOrderByUserIdGoodsId(userId, goodsId);
    }

    @Override
    @Transactional
    public OrderInfo createOrder(User user, GoodsDTO goods) {
        OrderInfo orderInfo = new OrderInfo();

        long orderId = SnowflakeIdWorker.generateId();

        orderInfo.setId(orderId);
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);

        redisUtil.set(OrderKey.getSeckillOrderByUidGid, "" + user.getNickname() + "_" + goods.getId(), seckillOrder);

        return orderInfo;
    }

}
