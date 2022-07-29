package com.plusl.core.service.rocketmq.seckill;

import com.alibaba.fastjson.JSON;
import com.plusl.core.service.GoodsService;
import com.plusl.core.service.OrderService;
import com.plusl.core.service.SeckillService;
import com.plusl.core.service.rocketmq.delcache.DelCacheMqProducer;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.dto.SeckillMessageDTO;
import com.plusl.framework.common.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: seckill-parent
 * @description: RocketMQ消费者
 * @author: PlusL
 * @create: 2022-07-09 11:27
 **/
@Service
@RocketMQMessageListener(topic = "seckill_queue", consumerGroup = "seckill_group")
public class SeckillMqConsumer implements RocketMQListener<String> {

    private static final Logger logger = LogManager.getLogger(SeckillMqConsumer.class);

    /**
     * 延时时间：预估读数据库数据业务逻辑的耗时，用来做延时双删
     */
    private static final int DELAY_MILLSECONDS = 1000;

    /**
     * 延时双删线程池
     * SynchronousQueue：创建具有非公平访问策略的 SynchronousQueue
     * 持续时间60 * second
     * ThreadPoolExecutor.DiscardOldestPolicy：丢弃线称队列的旧的任务，将新的任务添加
     */
    private static ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(0, 100,
            30L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.DiscardOldestPolicy());


    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    DelCacheMqProducer delCacheMqProducer;

    @Override
    public void onMessage(String s) {
        logger.info("receive message:" + s);
        SeckillMessageDTO smv = JSON.toJavaObject(JSON.parseObject(s), SeckillMessageDTO.class);
        User user = smv.getUser();
        long goodsId = smv.getGoodsId();
        GoodsDTO goods = goodsService.getGoodsDoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        try {
            seckillService.createOrderAndReduceStock(user, goods);
            deleteRedisStockCache(goodsId);
        } catch (Exception e) {
            logger.warn("MQ警告 SeckillMqConsumer 消费Message : {} 时出现异常: " ,s , e);
        }

    }

    /**
     * 延迟双删后
     * @param goodsId
     */
    private void deleteRedisStockCache(long goodsId) {
        // TODO: 单纯的删除不足以保证健壮性，可以采用第一次删除后发送到消息队列中再删一次，如果队列积压数据不多的话可以很快完成第二次删缓存。为防止删除失败可以
        //  利用 canal 订阅 MySQL binlog 日志监听写请求删除对应缓存
        boolean stepOne = goodsService.delStockCountCache(goodsId);
        // 延时双删
//        cachedThreadPool.execute(new delCacheByThread(goodsId));
        if (!stepOne) {
            delCacheMqProducer.sendSeckillMessage(goodsId);
        }

    }

    /**
     * 缓存再删除线程
     */
    private class delCacheByThread implements Runnable {
        private Long goodsId;
        public delCacheByThread(Long goodsId) {
            this.goodsId = goodsId;
        }
        @Override
        public void run() {
            try {
                logger.info("异步执行缓存再删除，商品id：[{}]， 首先休眠：[{}] 毫秒", goodsId, DELAY_MILLSECONDS);
                Thread.sleep(DELAY_MILLSECONDS);
                goodsService.delStockCountCache(goodsId);
                logger.info("再次删除商品id：[{}] 缓存", goodsId);
            } catch (Exception e) {
                logger.error("delCacheByThread执行出错", e);
            }
        }
    }
}
