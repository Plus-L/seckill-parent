package com.plusl.framework.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * @program: seckill-parent
 * @description: 秒杀订单实体类
 * @author: PlusL
 * @create: 2022-07-07 15:26
 **/

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Alias("seckillorder")
public class SeckillOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
