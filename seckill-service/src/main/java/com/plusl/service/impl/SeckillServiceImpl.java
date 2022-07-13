package com.plusl.service.impl;

import com.plusl.common.entity.OrderInfo;
import com.plusl.common.entity.SeckillOrder;
import com.plusl.common.entity.User;
import com.plusl.common.vo.GoodsVo;
import com.plusl.service.GoodsService;
import com.plusl.service.OrderService;
import com.plusl.service.SeckillService;
import com.plusl.service.redis.MiaoshaKey;
import com.plusl.service.redis.RedisService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: seckill-parent
 * @description: 秒杀服务类
 * @author: PlusL
 * @create: 2022-07-07 15:23
 **/
@Service
@DubboService(interfaceClass = SeckillService.class)
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Override
    @Transactional
    public OrderInfo doSeckill(User user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            return orderService.createOrder(user, goods);
        } else {
            //如果库存不存在则内存标记为true
            setGoodsOver(goods.getId());
            return null;
        }
    }

    @Override
    public long getMiaoshaResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        //秒杀成功
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }

}
