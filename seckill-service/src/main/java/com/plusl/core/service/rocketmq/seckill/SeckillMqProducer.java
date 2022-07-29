package com.plusl.core.service.rocketmq.seckill;

import com.alibaba.fastjson.JSON;
import com.plusl.core.service.rocketmq.RocketMqConfig;
import com.plusl.framework.common.dto.SeckillMessageDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @program: seckill-parent
 * @description: RocketMQ生产者
 * @author: PlusL
 * @create: 2022-07-09 11:05
 **/
@Service
public class SeckillMqProducer {

    private static final Logger logger = LogManager.getLogger(SeckillMqProducer.class);

    @Autowired
    RocketMQTemplate rocketMqTemplate;

    @PostConstruct
    public void defaultMQProducer() {

        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(RocketMqConfig.SECKILL_QUEUE);
        //指定NameServer地址，先写死试试
        producer.setNamesrvAddr(RocketMqConfig.DEFAULT_NAMESRV);
        producer.setVipChannelEnabled(false);
        //发送失败重试次数
        producer.setRetryTimesWhenSendFailed(3);
        try {
            producer.start();
            logger.info("Seckill RocketMq producer start!");
        } catch (MQClientException e) {
            logger.warn("MQ生产者启动失败 组名 [seckill_queue] 失败信息如下: ", e);
        }
    }

    public void sendNormalMessage(String message) {
        logger.info("send message:" + message);
        rocketMqTemplate.convertAndSend(RocketMqConfig.SECKILL_QUEUE, message);
    }

    public void sendSeckillMessage(SeckillMessageDTO seckillMessageDTO) {
        String msg = JSON.toJSONString(seckillMessageDTO);
        logger.info("Seckill-queue send message:" + msg);
        //在使用convertAndSend时，需要指定destination（此处为seckill_queue），否则报错：No default destination
        rocketMqTemplate.convertAndSend(RocketMqConfig.SECKILL_QUEUE, msg);
    }

}
