package com.plusl.core.service.rocketmq.delcache;

import com.alibaba.fastjson.JSON;
import com.plusl.core.service.rocketmq.RocketMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @program: seckill-parent
 * @description: 双删缓存队列生产者
 * @author: PlusL
 * @create: 2022-07-27 11:03
 **/
@Slf4j
@Service
public class DelCacheMqProducer {

    @Autowired
    RocketMQTemplate rocketMqTemplate;

    @PostConstruct
    public void defaultMQProducer() {

        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(RocketMqConfig.DELCACHE_QUEUE);
        //指定NameServer地址，先写死试试
        producer.setNamesrvAddr(RocketMqConfig.DEFAULT_NAMESRV);
        producer.setVipChannelEnabled(false);
        try {
            producer.start();
            log.info("DelCache RocketMq producer start!");
        } catch (MQClientException e) {
            log.error("DelCache RocketMq producer启动异常 错误信息 : ", e);
        }
    }

    public void sendSeckillMessage(Long goodsId) {
        String msg = JSON.toJSONString(goodsId);
        log.info("DelCache-queue send message : " + msg);
        rocketMqTemplate.convertAndSend(RocketMqConfig.DELCACHE_QUEUE, msg);
    }

}
