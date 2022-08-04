package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.SeckillOrder;
import com.plusl.core.facade.api.entity.User;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;

/**
 * @program: seckill-framework
 * @description: 订单Facade-api
 * @author: PlusL
 * @create: 2022-07-25 15:04
 **/
public interface OrderFacade {

    /**
     * 通过订单ID获取订单信息，用FacadeResult封装
     *
     * @param orderId 订单ID
     * @return FacadeResult封装的订单信息返回
     */
    FacadeResult<OrderInfo> getOrderById(long orderId);

    /**
     * 获取秒杀订单信息
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     * @return FacadeResult封装的秒杀订单返回
     */
    FacadeResult<SeckillOrder> getSeckillOrderByUserIdGoodsId(long userId, long goodsId);

    /**
     * 创建订单
     *
     * @param user  用户实体
     * @param goods 商品实体
     * @return FacadeResult封装的订单信息返回
     */
    FacadeResult<OrderInfo> createOrder(User user, GoodsDTO goods);

}
