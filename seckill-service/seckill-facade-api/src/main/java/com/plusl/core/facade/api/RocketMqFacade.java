package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.dto.SeckillMessageDTO;

/**
 * @program: seckill-framework
 * @description: RocketMqFacade
 * @author: PlusL
 * @create: 2022-07-25 15:26
 **/
public interface RocketMqFacade {

    FacadeResult<String> sendSeckillMessage(SeckillMessageDTO seckillMessageDTO);
}
