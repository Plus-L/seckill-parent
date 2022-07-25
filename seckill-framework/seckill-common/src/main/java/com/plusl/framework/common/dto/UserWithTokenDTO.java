package com.plusl.framework.common.dto;

import com.plusl.framework.common.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
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

    @NotEmpty
    private User user;
    @NotEmpty
    private String token;

}
