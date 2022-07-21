package com.plusl.framework.common.redis;

public class OrderKey extends BasePrefix {

    public static OrderKey getSeckillOrderByUidGid = new OrderKey("moug");

    public OrderKey(String prefix) {
        super(prefix);
    }

}
