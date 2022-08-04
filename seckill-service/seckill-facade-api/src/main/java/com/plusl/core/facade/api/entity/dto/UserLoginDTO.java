package com.plusl.core.facade.api.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @program: seckill-parent
 * @description: 用户登录DTO
 * @author: PlusL
 * @create: 2022-07-16 15:50
 **/
@Getter
@Setter
@ToString
public class UserLoginDTO implements Serializable {
    /**
     * 账号
     */
    @NotBlank(message = "登陆账号不能为空")
    @Length(min = 5, max = 16, message = "账号长度为 5-16 位")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    private String nickname;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

    public String getNickname() {
        return nickname;
    }

    public UserLoginDTO setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserLoginDTO setPassword(String password) {
        this.password = password;
        return this;
    }

}
