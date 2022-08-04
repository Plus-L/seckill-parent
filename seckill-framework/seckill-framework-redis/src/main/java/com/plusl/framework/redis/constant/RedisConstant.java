package com.plusl.framework.redis.constant;

/**
 * @program: seckill-parent
 * @description: RedisConstant
 * @author: PlusL
 * @create: 2022-07-29 15:38
 **/
public class RedisConstant {

    /**
     * 默认过期时间 一小时
     */
    public static final Long DEFAULT_EXPIRE_TIME = 3600L;
    /**
     * 永不过期，一般用于热点数据
     */
    public static final Long NEVER_EXPIRE = 0L;
    /**
     * 短存活 - 2分钟
     */
    public static final Long SHORT_EXPIRE_TIME = 120L;
    /**
     * 用户实体过期 - 60 * 60 * 24 * 3 三天
     */
    public static final Long USER_EXPIRE_TIME = 60 * 60 * 24 * 3L;
    /**
     * TOKEN过期时间 - 3天
     */
    public static final Long TOKEN_EXPIRE = 3600 * 24 * 3L;


}
