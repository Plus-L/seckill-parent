package com.plusl.core.facade.api.entity.dto;

import com.plusl.core.facade.api.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: seckill-framework
 * @description: 用户以及token
 * @author: PlusL
 * @create: 2022-07-25 14:09
 **/
@Getter
@Setter
@ToString
public class UserWithTokenDTO implements Serializable {

    /**
     * 用户实体
     */
    @NotNull(message = "用户实体不能为空")
    private User user;

    /**
     * token
     */
    @NotEmpty(message = "token不能为空")
    private String token;

}
