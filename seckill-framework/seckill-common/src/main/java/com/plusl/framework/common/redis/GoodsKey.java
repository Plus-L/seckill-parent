package com.plusl.framework.common.redis;

/**
 * @author LJH
 */
public class GoodsKey extends BasePrefix {

    public static GoodsKey getGoodsList = new GoodsKey(120, "goodsList:");
    public static GoodsKey getGoodsDetail = new GoodsKey(120, "goodsDetail:");
    public static GoodsKey getSeckillGoodsStock = new GoodsKey(0, "goodsStock:");

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

}