package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;

/**
 * @program: seckill-framework
 * @description: 订单Facade-api
 * @author: PlusL
 * @create: 2022-07-25 15:04
 **/
public interface OrderFacade {

    FacadeResult<OrderInfo> getOrderById(long id);

    FacadeResult<SeckillOrder> getSeckillOrderByUserIdGoodsId(long userId, long goodsId);

    FacadeResult<OrderInfo> createOrder(User user, GoodsDTO goods);

}
