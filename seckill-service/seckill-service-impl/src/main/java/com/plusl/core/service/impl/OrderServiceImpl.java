package com.plusl.core.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.SeckillOrder;
import com.plusl.core.facade.api.entity.User;
import com.plusl.core.service.OrderService;
import com.plusl.core.service.mapper.OrderMapper;
import com.plusl.core.service.util.SnowflakeIdWorker;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.framework.redis.RedisKeyUtils;
import com.plusl.framework.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.plusl.framework.redis.constant.RedisConstant.DEFAULT_EXPIRE_TIME;


/**
 * @program: seckill-parent
 * @description: 订单服务
 * @author: PlusL
 * @create: 2022-07-07 15:23
 **/
@Service
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
    public SeckillOrder getSeckillOrderByUserIdGoodsId(Long userId, Long goodsId) {
        //TODO：直接取数据库可能导致服务崩掉的问题，解决方法如加到缓存，加入到缓存还有缓存击穿的问题需要考虑
        // 决定采用缓存手段，第一此方法会被重复调用用来判断用户是否是重复秒杀，第二用户在购买后很有可能的操作是直接查看订单信息
        String prefixSeckillOrder = RedisKeyUtils.getPrefixSeckillOrder(userId, goodsId);
        SeckillOrder seckillOrder = redisUtil.get(prefixSeckillOrder, SeckillOrder.class);
        if (!ObjectUtil.isEmpty(seckillOrder)) {
            return seckillOrder;
        } else {
            SeckillOrder seckillOrderByDB = orderMapper.getSeckillOrderByUserIdGoodsId(userId, goodsId);
            redisUtil.setnx(prefixSeckillOrder, JSON.toJSONString(seckillOrderByDB));
            return seckillOrderByDB;
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public OrderInfo createOrder(User user, GoodsDTO goods) {
        OrderInfo orderInfo = new OrderInfo();

        long orderId = SnowflakeIdWorker.generateId();

        orderInfo.setId(orderId);
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getGoodsId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getGoodsId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);

        redisUtil.set(RedisKeyUtils.getPrefixSeckillOrder(user.getId(), goods.getGoodsId()),
                seckillOrder, DEFAULT_EXPIRE_TIME);

        return orderInfo;
    }

}
