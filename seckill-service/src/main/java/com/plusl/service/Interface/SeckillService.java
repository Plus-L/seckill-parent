package com.plusl.service;

import com.plusl.common.entity.OrderInfo;
import com.plusl.common.entity.User;
import com.plusl.common.vo.GoodsVo;

/**
 * @program: seckill-parent
 * @description: 秒杀服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:10
 **/
public interface SeckillService {

    public OrderInfo doSeckill(User user, GoodsVo goods);

    public long getSeckillResult(Long userId, long goodsId);

}
