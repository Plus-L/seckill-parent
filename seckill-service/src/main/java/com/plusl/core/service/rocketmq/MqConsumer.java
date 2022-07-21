package com.plusl.core.service.rocketmq;

import com.alibaba.fastjson.JSON;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.dto.SeckillMessageDTO;
import com.plusl.core.service.Interface.GoodsService;
import com.plusl.core.service.Interface.OrderService;
import com.plusl.core.service.Interface.SeckillService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: seckill-parent
 * @description: RocketMQ消费者
 * @author: PlusL
 * @create: 2022-07-09 11:27
 **/
@Service
@RocketMQMessageListener(topic = "seckill_queue", consumerGroup = "seckill_group")
public class MqConsumer implements RocketMQListener<String> {

    private static final Logger logger = LogManager.getLogger(MqConsumer.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Override
    public void onMessage(String s) {
        logger.info("receive message:" + s);
        SeckillMessageDTO smv = JSON.toJavaObject(JSON.parseObject(s), SeckillMessageDTO.class);
        User user = smv.getUser();
        long userId = user.getId();
        long goodsId = smv.getGoodsId();
        GoodsDTO goods = goodsService.getGoodsDoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        seckillService.createOrderAndReduceStock(user, goods);
    }

}
