package com.plusl.service.redis;

public class SeckillKey extends BasePrefix {

    public static SeckillKey isGoodsOver = new SeckillKey(0, "goodsOver");
    public static SeckillKey getSeckillPath = new SeckillKey(60, "mp");
    private SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

}
