package com.plusl.service.redis;

/**
 * @author LJH
 */
public class GoodsKey extends BasePrefix {

    public static GoodsKey getGoodsList = new GoodsKey(120, "goodsList:");
    public static GoodsKey getGoodsDetail = new GoodsKey(120, "goodsDetail:");
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0, "goodsStock:");

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

}
