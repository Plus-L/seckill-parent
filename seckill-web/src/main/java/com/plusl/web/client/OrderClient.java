package com.plusl.web.client;

import com.plusl.core.facade.api.OrderFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import com.plusl.web.utils.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @program: seckill-framework
 * @description: 订单Client
 * @author: PlusL
 * @create: 2022-07-25 15:13
 **/
@Service
public class OrderClient {

    @DubboReference(version = "1.0.0", timeout = 30000, check = false)
    OrderFacade orderFacade;

    public OrderInfo getOrderById(Long orderId) {
        FacadeResult<OrderInfo> result = orderFacade.getOrderById(orderId);
        Assert.isSuccess(result);
        return result.getData();
    }

    public SeckillOrder preventRepeatedSeckill(Long userId, Long goodsId) {
        FacadeResult<SeckillOrder> result = orderFacade.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        Assert.isSuccess(result);
        return result.getData();
    }

    public OrderInfo createOrder(User user, GoodsDTO goods) {
        FacadeResult<OrderInfo> result = orderFacade.createOrder(user, goods);
        Assert.isSuccess(result);
        return result.getData();
    }

}
