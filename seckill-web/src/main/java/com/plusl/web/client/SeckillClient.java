package com.plusl.web.client;

import com.plusl.core.facade.api.SeckillFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.User;
import com.plusl.web.utils.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @program: seckill-framework
 * @description: 秒杀Client
 * @author: PlusL
 * @create: 2022-07-23 17:29
 **/

@Service
public class SeckillClient {

    @DubboReference(version = "1.0.0", timeout = 30000, check = false)
    SeckillFacade seckillFacade;

    public OrderInfo createOrderAndReduceStock(User user, GoodsDTO goodsDTO) {
        FacadeResult<OrderInfo> result = seckillFacade.createOrderAndReduceStock(user, goodsDTO);
        Assert.isSuccess(result);
        return result.getData();
    }

    public Long getSeckillResult(Long userId, Long goodsId) {
        FacadeResult<Long> result = seckillFacade.getSeckillResult(userId, goodsId);
        Assert.isSuccess(result);
        return result.getData();
    }

}
