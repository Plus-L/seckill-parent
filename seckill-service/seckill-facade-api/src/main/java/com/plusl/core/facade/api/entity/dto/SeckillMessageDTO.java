package com.plusl.core.facade.api.entity.dto;

import com.plusl.core.facade.api.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: seckill-parent
 * @description: 秒杀信息传输类
 * @author: PlusL
 * @create: 2022-07-09 14:36
 **/

@Getter
@Setter
@ToString
public class SeckillMessageDTO implements Serializable {

    /**
     * 用户实体
     */
    @NotNull(message = "用户实体不能为空")
    private User user;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

}
