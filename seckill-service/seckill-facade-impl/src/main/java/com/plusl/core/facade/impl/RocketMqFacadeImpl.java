package com.plusl.core.facade.impl;

import com.plusl.core.facade.api.RocketMqFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.service.rocketmq.seckill.SeckillMqProducer;
import com.plusl.core.facade.api.entity.dto.SeckillMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.plusl.framework.common.enums.status.ResultStatus.SEND_FAIL;
import static com.plusl.framework.common.enums.status.ResultStatus.SEND_SUCCESS;

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
    public FacadeResult<String> sendSeckillMessage(SeckillMessageDTO seckillMessageDTO) {

        try {
            seckillMqProducer.sendSeckillMessage(seckillMessageDTO);
            return FacadeResult.success(SEND_SUCCESS.getMessage());
        } catch (Exception e) {
            log.warn("RocketMQ发送消息失败，详细信息如下：", e);
            return FacadeResult.fail(SEND_FAIL.getCode(), SEND_FAIL.getMessage());
        }
    }
}
