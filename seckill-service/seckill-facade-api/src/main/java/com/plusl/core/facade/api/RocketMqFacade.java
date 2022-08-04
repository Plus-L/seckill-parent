package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.dto.SeckillMessageDTO;

/**
 * @program: seckill-framework
 * @description: RocketMqFacade
 * @author: PlusL
 * @create: 2022-07-25 15:26
 **/
public interface RocketMqFacade {

    /**
     * 发送MQ信息
     *
     * @param seckillMessageDTO 秒杀信息传输类
     * @return Facade结果封装
     */
    FacadeResult<String> sendSeckillMessage(SeckillMessageDTO seckillMessageDTO);
}
