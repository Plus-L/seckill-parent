package com.plusl.core.service.impl;

import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.dto.SeckillMessageDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.redis.GoodsKey;
import com.plusl.framework.common.redis.RedisUtil;
import com.plusl.framework.common.redis.SeckillKey;
import com.plusl.core.service.GoodsService;
import com.plusl.core.service.OrderService;
import com.plusl.core.service.SeckillService;
import com.plusl.core.service.rocketmq.MqProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: seckill-parent
 * @description: 秒杀服务类
 * @author: PlusL
 * @create: 2022-07-07 15:23
 **/
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MqProducer mqProducer;

    @Override
    @Transactional
    public OrderInfo createOrderAndReduceStock(User user, GoodsDTO goodsDTO) {
        //减库存 下订单 写入秒杀订单
        Long goodsId = goodsDTO.getId();
        boolean success = goodsService.reduceStock(goodsId);
        if (success) {
            return orderService.createOrder(user, goodsDTO);
        } else {
            //如果库存不存在则内存标记为true
            setGoodsOver(goodsId);
            return null;
        }
    }

    @Override
    public Long getSeckillResult(Long userId, Long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        //秒杀成功
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1L;
            } else {
                return 0L;
            }
        }
    }

    @Override
    public OrderInfo doSeckill(User user, Long goodsId) {
        //TODO: 将controller层的业务处理迁移到Service中
        //判断是否已秒杀到了，防止重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return null;
        }

        //预减库存
        Long stock = redisUtil.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if (stock < 0) {
            return null;
        }

        SeckillMessageDTO seckillMessageDTO = new SeckillMessageDTO();
        seckillMessageDTO.setGoodsId(goodsId);
        seckillMessageDTO.setUser(user);
        mqProducer.sendSeckillMessage(seckillMessageDTO);
        //TODO: 返回值待定
        return null;
    }

    private void setGoodsOver(Long goodsId) {
        redisUtil.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisUtil.exists(SeckillKey.isGoodsOver, "" + goodsId);
    }

}
