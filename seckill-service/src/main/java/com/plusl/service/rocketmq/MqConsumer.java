package com.plusl.service.rocketmq;

import com.plusl.common.entity.User;
import com.plusl.common.vo.GoodsVo;
import com.plusl.common.vo.SeckillMessageVo;
import com.plusl.service.impl.GoodsServiceImpl;
import com.plusl.service.impl.OrderServiceImpl;
import com.plusl.service.impl.SeckillServiceImpl;
import com.plusl.service.redis.RedisService;
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
    RedisService redisService;

    @Autowired
    GoodsServiceImpl goodsService;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    SeckillServiceImpl miaoShaService;

    @Override
    public void onMessage(String s) {
        logger.info("receive message:" + s);
        SeckillMessageVo smv = RedisService.stringToBean(s, SeckillMessageVo.class);
        User user = smv.getUser();
        long userId = user.getId();
        long goodsId = smv.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
/*        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return;
        }*/
        //减库存 下订单 写入秒杀订单
        miaoShaService.doSeckill(user, goods);
    }

}
