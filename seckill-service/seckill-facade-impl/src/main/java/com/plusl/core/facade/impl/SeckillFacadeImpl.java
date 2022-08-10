package com.plusl.core.facade.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.plusl.core.facade.api.SeckillFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.service.SeckillService;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.plusl.framework.common.constant.CommonConstant.SECKILL_IN_LINE;
import static com.plusl.framework.common.constant.CommonConstant.SECKILL_SOLD_OUT;
import static com.plusl.framework.common.enums.status.ResultStatus.*;

/**
 * @program: seckill-framework
 * @description: SeckillFacade实现类
 * @author: PlusL
 * @create: 2022-07-23 16:23
 **/
@Slf4j
@Component
@DubboService(version = "1.0.0")
public class SeckillFacadeImpl implements SeckillFacade {

    @Autowired
    SeckillService seckillService;

    @Override
    @SentinelResource(blockHandler = "blockHandlerForCreateOrderAndReduceStock", fallback = "fallBackForCreateOrderAndReduceStock")
    public FacadeResult<OrderInfo> createOrderAndReduceStock(User user, GoodsDTO goodsDTO) {

        try {
            OrderInfo info = seckillService.createOrderAndReduceStock(user, goodsDTO);
            if (ObjectUtil.isEmpty(info)) {
                return FacadeResult.fail(SECKILL_FAIL.getCode(), SECKILL_FAIL.getMessage());
            }
            return FacadeResult.success(info);
        } catch (Exception e) {
            log.warn("方法 [createOrderAndReduceStock] 异常 异常信息：", e);
            return FacadeResult.fail(SECKILL_FAIL.getCode(), SECKILL_FAIL.getMessage());
        }
    }

    @Override
    public FacadeResult<String> getSeckillResult(Long userId, Long goodsId) {
        try {
            String seckillResult = seckillService.getSeckillResult(userId, goodsId);
            return FacadeResult.success(seckillResult);
        } catch (Exception e) {
            log.warn("方法 [getSeckillResult] 异常 异常信息：", e);
            return FacadeResult.fail(GET_RESULT_ERROR.getCode(), GET_RESULT_ERROR.getMessage());
        }
    }

    public FacadeResult blockHandlerForCreateOrderAndReduceStock(User user, GoodsDTO goodsDTO, BlockException e) {
        FacadeResult facadeResult = fail();
        log.warn("创建订单并减少库存 触发流控 请求信息 : {} {} 返回信息 : {}", JSON.toJSONString(user), JSON.toJSONString(goodsDTO), JSON.toJSONString(facadeResult), e);
        return facadeResult;
    }

    public FacadeResult fallBackForCreateOrderAndReduceStock(User user, GoodsDTO goodsDTO, Throwable throwable) {
        FacadeResult facadeResult = fail();
        log.warn("创建订单并减少库存 触发熔断降级 请求信息 : {} {} 返回信息 : {}", JSON.toJSONString(user), JSON.toJSONString(goodsDTO), JSON.toJSONString(facadeResult), throwable);
        return facadeResult;
    }

    private FacadeResult fail() {
        FacadeResult result = new FacadeResult();
        result.setErrorCode(NET_BUSY.getCode());
        result.setMessage(NET_BUSY.getMessage());
        return result;
    }

}
