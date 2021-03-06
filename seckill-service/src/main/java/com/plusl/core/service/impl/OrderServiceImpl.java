package com.plusl.core.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.redis.RedisKeyUtils;
import com.plusl.framework.common.redis.RedisUtil;
import com.plusl.core.service.OrderService;
import com.plusl.core.service.mapper.OrderMapper;
import com.plusl.core.service.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.plusl.framework.common.redis.RedisConstant.DEFAULT_EXPIRE_TIME;

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
    @Transactional(rollbackFor = Exception.class)
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

        redisUtil.set(RedisKeyUtils.getPrefixSeckillOrder(user.getId(), goods.getId()), seckillOrder, DEFAULT_EXPIRE_TIME);

        return orderInfo;
    }

}
