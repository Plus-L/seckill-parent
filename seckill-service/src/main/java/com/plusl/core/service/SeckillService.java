package com.plusl.core.service;

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

    /**
     * 减库存并创建订单
     *
     * @param user 用户实体
     * @param goodsDTO 商品DTO
     * @return 订单信息实体
     */
    OrderInfo createOrderAndReduceStock(User user, GoodsDTO goodsDTO);

    /**
     * 获取秒杀的结果
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     * @return 三种输出：
     * 成功-返回订单ID
     * 失败 库存空-返回(0)；异常失败-返回(-1)
     */
    Long getSeckillResult(Long userId, Long goodsId);

}
