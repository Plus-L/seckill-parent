package com.plusl.framework.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

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
public class SeckillOrder implements Serializable {

    /**
     * 秒杀订单ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long goodsId;
}
