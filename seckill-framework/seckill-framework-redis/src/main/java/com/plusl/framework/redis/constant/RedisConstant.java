package com.plusl.framework.redis.constant;

/**
 * @program: seckill-parent
 * @description: RedisConstant
 * @author: PlusL
 * @create: 2022-07-29 15:38
 **/
public class RedisConstant {

    public static final Long DEFAULT_EXPIRE_TIME = 3600L;
    public static final Long NEVER_EXPIRE = 0L;
    public static final Long SHORT_EXPIRE_TIME = 120L;
    /**
     * 用户实体过期 - 60 * 60 * 24 * 3 三天
     */
    public static final Long USER_EXPIRE_TIME = 60 * 60 * 24 * 3L;
    public static final Long TOKEN_EXPIRE = 3600 * 24 * 3L;


}
