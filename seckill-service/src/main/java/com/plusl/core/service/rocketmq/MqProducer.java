package com.plusl.core.service.rocketmq;

import com.alibaba.fastjson.JSON;
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
public class MqProducer {

    private static final Logger logger = LogManager.getLogger(MqProducer.class);

    @Autowired
    RocketMQTemplate rocketMqTemplate;

    private DefaultMQProducer producer;

    @PostConstruct
    public void defaultMQProducer() {

        //生产者的组名
        producer = new DefaultMQProducer("seckill_group");
        //指定NameServer地址，先写死试试
        producer.setNamesrvAddr("localhost:9876");
        producer.setVipChannelEnabled(false);
        try {
            producer.start();
            logger.info("RocketMq producer start!");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void sendNormalMessage(String message) {
        logger.info("send message:" + message);
        rocketMqTemplate.convertAndSend(RocketMqConfig.MIAOSHA_QUEUE, message);
    }

    public void sendSeckillMessage(SeckillMessageDTO seckillMessageDTO) {
        String msg = JSON.toJSONString(seckillMessageDTO);
        logger.info("send message:" + msg);
        //在使用convertAndSend时，需要指定destination（此处为seckill_queue），否则报错：No default destination
        rocketMqTemplate.convertAndSend("seckill_queue", msg);
    }

}
