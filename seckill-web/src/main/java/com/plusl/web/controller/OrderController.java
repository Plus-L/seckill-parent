package com.plusl.web.controller;

import com.plusl.common.entity.OrderInfo;
import com.plusl.common.entity.User;
import com.plusl.common.enums.result.Result;
import com.plusl.common.enums.status.ResultStatus;
import com.plusl.common.vo.GoodsVo;
import com.plusl.common.vo.OrderDetailVo;
import com.plusl.service.GoodsService;
import com.plusl.service.OrderService;
import com.plusl.service.UserService;
import com.plusl.service.redis.RedisService;
import com.plusl.web.interceptor.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: seckill-parent
 * @description: 订单控制类
 * @author: PlusL
 * @create: 2022-07-08 11:41
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    public Result<OrderDetailVo> orderDetailInfo(User user, @RequestParam("orderId") long orderId) {

        Result<OrderDetailVo> result = Result.build();
        //TODO: 以注解的方式判断user是否为null
        if (user == null) {
            return Result.error(ResultStatus.SESSION_ERROR);
        }

        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(ResultStatus.ORDER_NOT_EXIST);
        }

        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoods(goods);
        result.setData(orderDetailVo);


        return result;
    }

}
