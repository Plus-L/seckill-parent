package com.plusl.framework.common.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * @program: seckill-parent
 * @description: Redis- JedisPool- Bean
 * @author: PlusL
 * @create: 2022-07-20 14:37
 **/
@Configuration
public class RedisPoolFactory {

    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPool jedisPool = new JedisPool();
        return jedisPool;
    }
}
