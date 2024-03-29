package com.plusl.framework.redis.lua;

import com.plusl.framework.redis.RedisService;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: seckill-parent
 * @description: LUA脚本限流
 * @author: PlusL
 * @create: 2022-07-15 15:45
 **/
public class RedisLimitRateWithLua {
    public static boolean accquire() throws IOException, URISyntaxException {

        RedisService redisService = new RedisService();
        Jedis jedis = redisService.getJedisObject();

        //限流Key 每秒一个；设置的限流大小：1；限流Key过期时间：2s
        String lua =
                "local key = KEYS[1] " +
                        " local limit = tonumber(ARGV[1]) " +
                        " local current = tonumber(redis.call('get', key) or '0')" +
                        " if current + 1 > limit " +
                        " then  return 0 " +
                        " else " +
                        " redis.call('INCRBY', key,'1')" +
                        " redis.call('expire', key,'2') " +
                        " end return 1 ";

        String key = "ip:" + System.currentTimeMillis() / 1000;
        String limit = "3";
        List<String> keys = new ArrayList<>();
        keys.add(key);
        List<String> args = new ArrayList<>();
        args.add(limit);
        String luaScript = jedis.scriptLoad(lua);
        Long result = (Long) jedis.evalsha(luaScript, keys, args);
        return result == 1;
    }
}
