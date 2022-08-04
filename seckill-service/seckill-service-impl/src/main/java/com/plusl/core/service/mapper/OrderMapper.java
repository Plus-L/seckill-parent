package com.plusl.core.service.mapper;


import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author LJH
 * @description: 订单Mapper类
 */
@Mapper
public interface OrderMapper {

    /**
     * 获取秒杀订单
     *
     * @param userNickName 用户名
     * @param goodsId      商品ID
     * @return 秒杀订单
     */
    SeckillOrder getSeckillOrderByUserIdGoodsId(@Param("userNickName") long userNickName,
                                                @Param("goodsId") long goodsId);

    /**
     * 插入普通订单
     *
     * @param orderInfo 订单信息
     * @return 0-失败 1-成功
     */
    long insert(OrderInfo orderInfo);

    /**
     * 插入秒杀订单
     *
     * @param seckillOrder 秒杀订单
     * @return 是否成功
     */
    int insertSeckillOrder(SeckillOrder seckillOrder);

    /**
     * 通过OrderId获取Order
     *
     * @param orderId 订单ID
     * @return 订单信息
     */
    OrderInfo getOrderById(@Param("orderId") long orderId);

    /**
     * 删除订单（订单状态置0）
     *
     * @return 是否成功
     */
    int closeOrderByOrderInfo();

}
