package com.plusl.core.service;

import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;

/**
 * @program: seckill-parent
 * @description: 订单服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:12
 **/
public interface OrderService {

    /**
     * 通过orderId获取订单
     *
     * @param orderId 订单ID
     * @return 订单
     */
    OrderInfo getOrderById(Long orderId);

    /**
     * 通过用户ID和商品ID获取秒杀订单
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     * @return 秒杀订单
     */
    SeckillOrder getSeckillOrderByUserIdGoodsId(Long userId, Long goodsId);

    /**
     * 创建订单
     *
     * @param user  用户实体
     * @param goods 商品
     * @return 订单信息
     */
    OrderInfo createOrder(User user, GoodsDTO goods);


}
