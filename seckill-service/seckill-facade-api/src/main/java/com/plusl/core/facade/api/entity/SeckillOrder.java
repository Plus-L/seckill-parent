package com.plusl.core.facade.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "秒杀订单ID不能为空")
    private Long id;

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private Long userId;

    /**
     * 订单ID
     */
    @NotBlank(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 商品ID
     */
    @NotBlank(message = "订单ID不能为空")
    private Long goodsId;
}
