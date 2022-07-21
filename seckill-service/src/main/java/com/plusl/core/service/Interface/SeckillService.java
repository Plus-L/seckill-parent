package com.plusl.core.service.Interface;

import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.User;

/**
 * @program: seckill-parent
 * @description: 秒杀服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:10
 **/
public interface SeckillService {

    OrderInfo createOrderAndReduceStock(User user, GoodsDTO goodsDTO);

    long getSeckillResult(Long userId, long goodsId);

    OrderInfo doSeckill(User user, Long goodsId);

}
