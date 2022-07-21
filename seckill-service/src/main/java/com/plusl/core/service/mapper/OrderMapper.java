package com.plusl.core.service.mapper;


import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LJH
 * @description: 订单Mapper类
 */
@Mapper
public interface OrderMapper {

    SeckillOrder getSeckillOrderByUserIdGoodsId(@Param("userNickName") long userNickName,
                                                @Param("goodsId") long goodsId);

    long insert(OrderInfo orderInfo);

    int insertSeckillOrder(SeckillOrder seckillOrder);

    OrderInfo getOrderById(@Param("orderId") long orderId);

    List<OrderInfo> selectOrderStatusByCreateTime(@Param("status") Integer status,
                                                  @Param("createDate") String createDate);

    int closeOrderByOrderInfo();

}
