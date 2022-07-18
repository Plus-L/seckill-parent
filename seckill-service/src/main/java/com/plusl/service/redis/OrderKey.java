package com.plusl.service.redis;

public class OrderKey extends com.plusl.service.redis.BasePrefix {

    public static OrderKey getSeckillOrderByUidGid = new OrderKey("moug");

    public OrderKey(String prefix) {
        super(prefix);
    }

}
