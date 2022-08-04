package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.User;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;

/**
 * @program: seckill-framework
 * @description: 秒杀Facade层
 * @author: PlusL
 * @create: 2022-07-23 16:17
 **/
public interface SeckillFacade {

    /**
     * 削减库存并创建订单
     *
     * @param user     用户实体
     * @param goodsDTO 商品传输类
     * @return FacadeResult封装订单信息
     */
    FacadeResult<OrderInfo> createOrderAndReduceStock(User user, GoodsDTO goodsDTO);

    /**
     * 获取秒杀结果
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     * @return 秒杀结果
     * 成功-返回订单ID
     * 失败 库存空-返回(0)；异常失败-返回(-1)
     */
    FacadeResult<Long> getSeckillResult(Long userId, Long goodsId);


}
