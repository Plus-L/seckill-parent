package com.plusl.core.facade.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.plusl.core.facade.api.OrderFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.service.OrderService;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.plusl.framework.common.enums.status.ResultStatus.*;

/**
 * @program: seckill-framework
 * @description: 订单Facade接口实现类
 * @author: PlusL
 * @create: 2022-07-25 15:05
 **/
@Slf4j
@Component
@DubboService(version = "1.0.0")
public class OrderFacadeImpl implements OrderFacade {

    @Autowired
    OrderService orderService;

    @Override
    public FacadeResult<OrderInfo> getOrderById(long id) {
        try {
            OrderInfo orderInfo = orderService.getOrderById(id);
            if (ObjectUtil.isEmpty(orderInfo)) {
                return FacadeResult.fail(ORDER_NOT_EXIST.getCode(), ORDER_NOT_EXIST.getMessage());
            }
            return FacadeResult.success(orderInfo);
        } catch (Exception e) {
            log.warn("方法 [getOrderById] 通过ID获取订单异常 异常信息：", e);
            return FacadeResult.fail(GET_ORDER_ERROR.getCode(), GET_ORDER_ERROR.getMessage());
        }
    }

    @Override
    public FacadeResult<SeckillOrder> getSeckillOrderByUserIdGoodsId(long userId, long goodsId) {
        try {
            SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
            if (ObjectUtil.isEmpty(order)) {
                return FacadeResult.fail(ORDER_NOT_EXIST.getCode(), ORDER_NOT_EXIST.getMessage());
            }
            return FacadeResult.success(order);
        } catch (Exception e) {
            log.warn("方法 [getSeckillOrderByUserIdGoodsId] 通过ID获取秒杀订单异常 异常信息：", e);
            return FacadeResult.fail(GET_ORDER_ERROR.getCode(), GET_ORDER_ERROR.getMessage());
        }
    }

    @Override
    @SentinelResource(blockHandler = "blockHandlerForCreateOrder")
    public FacadeResult<OrderInfo> createOrder(User user, GoodsDTO goods) {
        try {
            OrderInfo order = orderService.createOrder(user, goods);
            if (ObjectUtil.isEmpty(order)) {
                return FacadeResult.fail(CREAT_ORDER_FAIL.getCode(), CREAT_ORDER_FAIL.getMessage());
            }
            return FacadeResult.success(order);
        } catch (Exception e) {
            log.warn("方法 [createOrder] 创建订单异常 异常信息：", e);
            return FacadeResult.fail(CREAT_ORDER_FAIL.getCode(), CREAT_ORDER_FAIL.getMessage());
        }
    }

    public FacadeResult blockHandlerForCreateOrder(User user, GoodsDTO goodsDTO, BlockException e) {
        FacadeResult facadeResult = fail();
        log.warn("创建订单 触发流控 请求信息 : {} {} 返回信息 : {}", JSON.toJSONString(user), JSON.toJSONString(goodsDTO), JSON.toJSONString(facadeResult), e);
        return facadeResult;
    }

    private FacadeResult fail() {
        FacadeResult result = new FacadeResult();
        result.setErrorCode(NET_BUSY.getCode());
        result.setMessage(NET_BUSY.getMessage());
        return result;
    }
}
