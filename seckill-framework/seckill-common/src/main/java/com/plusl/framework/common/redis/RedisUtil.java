package com.plusl.framework.common.redis;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RedisUtil {

    @Autowired
    JedisPool jedisPool;

    /**
     * 如果不存在则设置。
     * 时间复杂度：O(1)
     * 参数：key – value
     *
     * @param key 键
     * @param value 值
     * @return 如果设置了键则为 1 如果未设置键则为 0
     */
    public Long setnx(String key, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("expire key:{} error", key, e);
            jedisPool.returnResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;

    }

    /**
     * 设置key的有效期，单位是秒
     * 在指定键上设置超时。超时后，密钥将被服务器自动删除。
     * 易失性密钥像其他密钥一样存储在磁盘上，超时也像数据集的所有其他方面一样持久。
     * 从 Redis 2.1.3 开始，可以更新已设置过期键的超时值。也可以使用 PERSIST 命令完全撤消过期，将密钥变为普通密钥
     *
     * @param key 键
     * @param seconds 秒
     * @return 1: 成功设置超时 0: Key不存在（2.1.3之前可能是Key已被设置超时，此处已是5.0.14）
     */
    public Long expire(String key, Long seconds) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error("expire key:{} error", key, e);
            jedisPool.returnBrokenResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 获取当个对象
     */
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String str = jedis.get(key);
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        } catch (Exception e) {
            log.error("Jedis Exception happen when 'get object':{}", key, e);
            return null;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 获取指定键的值。如果键不存在，则返回特殊值“nil”。如果存储在 key 的值不是字符串，则会返回错误
     *
     * @param key 键
     * @return value / 键不存在"nil"
     */
    public String get(String key) {
        Jedis jedis = null;
        String result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error 错误信息: ", key, e);
            jedisPool.returnBrokenResource(jedis);
            return null;
        }
        jedisPool.returnResource(jedis);
        return result;
    }

    /**
     * GETSET是一个原子设置这个值并返回旧值的命令。将 key 设置为字符串值并返回存储在 key 中的旧值
     * @param key 键
     * @param value 值
     * @return 获取到的旧值
     */
    public String getSet(String key, String value) {
        Jedis jedis = null;
        String result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("expire key:{} error", key, e);
            jedisPool.returnBrokenResource(jedis);
            return null;
        }
        jedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置对象
     */
    public <T> boolean set(String key, T value, Long seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = JSON.toJSONString(value);
            if (str.length() <= 0) {
                return false;
            }
            if (seconds <= 0) {
                jedis.set(key, str);
            } else {
                jedis.setex(key, seconds, str);
            }
            return true;
        } catch (Exception e) {
            log.error("Jedis Error happen when set key:{} value:{}", key, value, e);
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     */
    public <T> boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            log.error("Jedis Error happen when 'exists' key:{}", key, e);
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    public Jedis getJedisObject() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis;
        } catch (Exception e) {
            log.error("获取Jedis对象失败", e);
            return null;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除
     * 删除指定的键。如果给定键不存在，则不会对该键执行任何操作。
     * 该命令返回删除的键数。时间复杂度：O(1) 参数：keys – 返回：整数回复，
     * 具体来说：如果删除了一个或多个键，则为大于 0 的整数；如果指定的键都不存在，则为 0
     */
    public boolean delete(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long ret = jedis.del(key);
            return ret > 0;
        } catch (Exception e) {
            log.error("Jedis Error happen when 'delete' keyPrefix: {} key: {}", key, e);
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 原子加一
     *
     * @param key 键
     * @return 加一后对应的值
     */
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            log.error("Jedis Error happen when 'incr' key : {}", key, e);
            return -1L;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 原子减一
     * @param key 键
     * @return 更新后的新值
     */
    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // decr使用时需要注意。如果键不存在或包含错误类型的值，请在执行减量操作之前将键设置为“0”值。
            return jedis.decr(key);
        } catch (Exception e) {
            log.error("Jedis Error happen when 'decr' key:{}", key, e);
            return -1L;
        } finally {
            returnToPool(jedis);
        }
    }

    public Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            jedisPool.returnBrokenResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;
    }


    public List<String> scanKeys(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = new ArrayList<>();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match("*" + key + "*");
            sp.count(100);
            do {
                ScanResult<String> ret = jedis.scan(cursor, sp);
                List<String> result = ret.getResult();
                if (result != null && result.size() > 0) {
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getCursor();
            } while (!"0".equals(cursor));
            return keys;
        } catch (Exception e) {
            log.error("Jedis Error happen when 'decr' key:{}", key, e);
            return null;
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
