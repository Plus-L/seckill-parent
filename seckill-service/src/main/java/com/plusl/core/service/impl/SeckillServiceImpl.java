package com.plusl.core.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.plusl.core.service.GoodsService;
import com.plusl.core.service.OrderService;
import com.plusl.core.service.SeckillService;
import com.plusl.core.service.rocketmq.seckill.SeckillMqProducer;
import com.plusl.framework.common.convert.goods.GoodsMapStruct;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.redis.RedisKeyUtils;
import com.plusl.framework.common.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    RedisUtil redisUtil;

    @Autowired
    SeckillMqProducer seckillMqProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SentinelResource(blockHandler = "blockHandlerForCreateOrderAndReduceStock", fallback = "fallBackForCreateOrderAndReduceStock")
    public OrderInfo createOrderAndReduceStock(User user, GoodsDTO goodsDTO) {
        //减库存 下订单 写入秒杀订单
        Long goodsId = goodsDTO.getId();
        boolean success = goodsService.reduceOneStock(GoodsMapStruct.INSTANCE.toSeckillGoods(goodsDTO));
        if (success) {
            log.info("DB 扣除库存成功 用户ID : {} 商品ID : {}", user.getId(), goodsId);
            return orderService.createOrder(user, goodsDTO);
        } else {
            //如果库存不存在则内存标记为true
            log.warn("DB 扣除库存失败 用户ID : {} 商品ID : {}", user.getId(), goodsId);
            return null;
        }
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
    public Long getSeckillResult(Long userId, Long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        //秒杀成功
        if (order != null) {
            return order.getOrderId();
        } else {
            String s = redisUtil.get(RedisKeyUtils.getSeckillGoodsStockPrefix(goodsId));
            int stock = Integer.parseInt(s);
            // 库存大于0即表示异常结束，小于等于0则库存空退出
            if (stock > 0) {
                return -1L;
            } else {
                return 0L;
            }
        }
    }

}
