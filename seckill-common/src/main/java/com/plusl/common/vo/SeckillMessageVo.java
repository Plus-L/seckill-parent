package com.plusl.common.vo;

import com.plusl.common.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: seckill-parent
 * @description: UserId GoodsId
 * @author: PlusL
 * @create: 2022-07-09 14:36
 **/

@Getter
@Setter
public class SeckillMessageVo {

    private User user;
    private Long goodsId;

}
