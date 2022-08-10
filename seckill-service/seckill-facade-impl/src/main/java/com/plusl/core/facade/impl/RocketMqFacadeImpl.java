package com.plusl.core.facade.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.plusl.core.facade.api.RocketMqFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.core.service.rocketmq.seckill.SeckillMqProducer;
import com.plusl.core.facade.api.entity.dto.SeckillMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.plusl.framework.common.enums.status.ResultStatus.*;
import static com.plusl.framework.common.enums.status.ResultStatus.NET_BUSY;

/**
 * @program: seckill-framework
 * @description: RocketMqFacadeImpl
 * @author: PlusL
 * @create: 2022-07-25 15:29
 **/
@Slf4j
@Component
@DubboService(version = "1.0.0")
public class RocketMqFacadeImpl implements RocketMqFacade {

    @Autowired
    SeckillMqProducer seckillMqProducer;

    @Override
    @SentinelResource(blockHandler = "blockHandlerForSedSeckillMsg")
    public FacadeResult<String> sendSeckillMessage(SeckillMessageDTO seckillMessageDTO) {

        try {
            seckillMqProducer.sendSeckillMessage(seckillMessageDTO);
            return FacadeResult.success(SEND_SUCCESS.getMessage());
        } catch (Exception e) {
            log.warn("RocketMQ发送消息失败，详细信息如下：", e);
            return FacadeResult.fail(SEND_FAIL.getCode(), SEND_FAIL.getMessage());
        }
    }

    public FacadeResult<String> blockHandlerForSedSeckillMsg(SeckillMessageDTO seckillMessageDTO, BlockException e) {
        FacadeResult result = new FacadeResult();
        result.setErrorCode(NET_BUSY.getCode());
        result.setMessage(NET_BUSY.getMessage());
        log.warn("通过商品ID削减库存 触发流控 请求信息 : {}  返回信息 : {}",
                JSON.toJSONString(seckillMessageDTO), JSON.toJSONString(result), e);
        return result;
    }
}
