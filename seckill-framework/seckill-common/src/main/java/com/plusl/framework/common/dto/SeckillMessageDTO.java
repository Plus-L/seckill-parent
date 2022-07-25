package com.plusl.framework.common.dto;

import com.plusl.framework.common.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: seckill-parent
 * @description: UserId GoodsId
 * @author: PlusL
 * @create: 2022-07-09 14:36
 **/

@Getter
@Setter
@ToString
public class SeckillMessageDTO implements Serializable {

    private User user;
    private Long goodsId;

}
