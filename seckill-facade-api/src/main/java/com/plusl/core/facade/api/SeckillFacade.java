package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.User;

/**
 * @program: seckill-framework
 * @description: 秒杀Facade层
 * @author: PlusL
 * @create: 2022-07-23 16:17
 **/
public interface SeckillFacade {

    FacadeResult<OrderInfo> createOrderAndReduceStock(User user, GoodsDTO goodsDTO);

    FacadeResult<Long> getSeckillResult(Long userId, Long goodsId);


}
