package com.plusl.common.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: seckill-parent
 * @description: 登录Vo
 * @author: PlusL
 * @create: 2022-07-05 17:43
 **/

@Getter
@Setter
public class LoginVo implements Serializable {

    @NotNull
    private String nickname;

    @NotNull
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "nickName='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
