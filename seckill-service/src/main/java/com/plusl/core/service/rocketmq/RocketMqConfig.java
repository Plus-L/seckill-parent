package com.plusl.core.service.rocketmq;

import org.springframework.context.annotation.Configuration;

/**
 * @program: seckill-parent
 * @description: RocketMQ配置类
 * @author: PlusL
 * @create: 2022-07-09 10:58
 **/
@Configuration
public class RocketMqConfig {

    public static final String DEFAULT_NAMESRV = "localhost:9876";

    public static final String SECKILL_QUEUE = "seckill_queue";

    public static final String DELCACHE_QUEUE = "delcache_queue";

}
