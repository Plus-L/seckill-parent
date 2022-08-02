package com.plusl.framework.redis;

/**
 * @program: seckill-parent
 * @description: 生成Redis key工具类
 * @author: PlusL
 * @create: 2022-07-28 10:09
 **/
public class RedisKeyUtils {

    /**
     * 分隔符
     */
    private static final String SPLIT = ":";
    /**
     * 秒杀订单SeckillOrder缓存：前缀
     */
    private static final String PREFIX_SECKILL_ORDER = "seckill:order";
    /**
     * 秒杀商品库存缓存：前缀
     */
    private static final String PREFIX_SECKILL_GOODS_STOCK = "seckill:goods:stock";
    /**
     * 商品实体前缀
     */
    private static final String PREFIX_SECKILL_GOODS = "seckill:goods";
    /**
     * key：token  value：User
     */
    private static final String PREFIX_USER_TOKEN = "user:token";
    /**
     * key：username  value：User
     */
    private static final String PREFIX_USER_NAME = "user:username";


    public static String getPrefixSeckillOrder(Long userId, Long goodsId) {
        return PREFIX_SECKILL_ORDER + SPLIT + userId + SPLIT + goodsId;
    }

    public static String getSeckillGoodsStockPrefix(Long goodsId) {
        return PREFIX_SECKILL_GOODS_STOCK + SPLIT + goodsId;
    }

    public static String getSeckillGoodsPrefix(Long goodsId) {
        return PREFIX_SECKILL_GOODS + SPLIT + goodsId;
    }

    public static String getPrefixUserToken(String token) {
        return PREFIX_USER_TOKEN + SPLIT + token;
    }

    public static String getPrefixUserByName(String userName) {
        return PREFIX_USER_NAME + SPLIT + userName;
    }

}
