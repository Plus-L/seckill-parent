package com.plusl.web.client;

import com.plusl.core.facade.api.RocketMqFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.dto.SeckillMessageDTO;
import com.plusl.web.utils.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @program: seckill-framework
 * @description: RocketMqClient
 * @author: PlusL
 * @create: 2022-07-25 15:47
 **/
@Service
public class RocketMqClient {

    @DubboReference(version = "1.0.0", timeout = 30000, check = false)
    RocketMqFacade rocketMqFacade;

    public String sendSeckillMessage(SeckillMessageDTO seckillMessageDTO) {
        FacadeResult<String> result = rocketMqFacade.sendSeckillMessage(seckillMessageDTO);
        Assert.isSuccess(result);
        return result.getData();
    }


}
