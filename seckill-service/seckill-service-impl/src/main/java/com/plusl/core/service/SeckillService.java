package com.plusl.core.service;

import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.User;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;

/**
 * @program: seckill-parent
 * @description: 秒杀服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:10
 **/
public interface SeckillService {

    /**
     * 减库存并创建订单
     * 关于事务：这里选择的 隔离级别-可重读 / 事务传播-默认（若当前存在事务则加入，不存在则创建一个，当加入的事务有一个回滚则集体回滚）
     * rollBackFor使用默认，代表回滚RuntimeException以及Error
     *
     * @param user     用户实体
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
