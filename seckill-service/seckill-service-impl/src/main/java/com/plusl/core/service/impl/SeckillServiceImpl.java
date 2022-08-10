package com.plusl.core.service.impl;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.SeckillOrder;
import com.plusl.core.facade.api.entity.User;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.core.service.GoodsService;
import com.plusl.core.service.OrderService;
import com.plusl.core.service.SeckillService;
import com.plusl.core.service.convert.goods.GoodsMapStruct;
import com.plusl.core.service.rocketmq.seckill.SeckillMqProducer;
import com.plusl.framework.common.constant.CommonConstant;
import com.plusl.framework.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: seckill-parent
 * @description: 秒杀服务类
 * @author: PlusL
 * @create: 2022-07-07 15:23
 **/
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillMqProducer seckillMqProducer;

    // TODO: 限流放到Facade层中，一般限制入口，不限制Service
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public OrderInfo createOrderAndReduceStock(User user, GoodsDTO goodsDTO) {
        //减库存 下订单 写入秒杀订单
        Long goodsId = goodsDTO.getGoodsId();
        boolean success = goodsService.reduceOneStock(GoodsMapStruct.INSTANCE.toSeckillGoods(goodsDTO));
        if (success) {
            log.info("DB 扣除库存成功 用户ID : {} 商品ID : {}", user.getId(), goodsId);
            return orderService.createOrder(user, goodsDTO);
        }

        // TODO : 日志要打在关键的地方，不要乱打，日志打多了对性能会产生影响
        log.warn("DB 扣除库存失败 用户ID : {} 商品ID : {}", user.getId(), goodsId);
        return null;
    }

    public OrderInfo blockHandlerForCreateOrderAndReduceStock(User user, GoodsDTO goodsDTO, BlockException e) {
        OrderInfo orderInfo = new OrderInfo();
        log.warn("创建订单并减少库存 触发流控 请求信息 : {} {} 返回信息 : {}", JSON.toJSONString(user), JSON.toJSONString(goodsDTO), JSON.toJSONString(orderInfo), e);
        return orderInfo;
    }

    public OrderInfo fallBackForCreateOrderAndReduceStock(User user, GoodsDTO goodsDTO, Throwable throwable) {
        OrderInfo orderInfo = new OrderInfo();
        log.warn("创建订单并减少库存 触发熔断降级 请求信息 : {} {} 返回信息 : {}", JSON.toJSONString(user), JSON.toJSONString(goodsDTO), JSON.toJSONString(orderInfo), throwable);
        return orderInfo;
    }

    @Override
    public String getSeckillResult(Long userId, Long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        //TODO : 如何区分三种状态：1.排队中； 2.秒杀失败
        if (order != null) {
            return CommonConstant.SECKILL_SECCESS;
        }
        if (goodsService.getGoodsDtoByGoodsId(goodsId).getStockCount() <= 0) {
            return CommonConstant.SECKILL_SOLD_OUT;
        }
        return CommonConstant.SECKILL_IN_LINE;

    }
}
