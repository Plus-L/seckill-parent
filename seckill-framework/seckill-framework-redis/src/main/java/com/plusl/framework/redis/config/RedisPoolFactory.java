package com.plusl.framework.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * @program: seckill-parent
 * @description: Redis- JedisPool- Bean 后续可以继续在该工厂类中拓展其他的Redis客户端
 * @author: PlusL
 * @create: 2022-07-20 14:37
 **/
@Configuration
public class RedisPoolFactory {

    @Bean
    public JedisPool jedisPoolFactory() {
        return new JedisPool();
    }
}
