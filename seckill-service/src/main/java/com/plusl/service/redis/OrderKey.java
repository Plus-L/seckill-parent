package com.plusl.service.redis;

public class OrderKey extends com.plusl.service.redis.BasePrefix {

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");

    public OrderKey(String prefix) {
        super(prefix);
    }

}
