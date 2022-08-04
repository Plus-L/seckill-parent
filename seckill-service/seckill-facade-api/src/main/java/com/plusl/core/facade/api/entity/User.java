package com.plusl.core.facade.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: seckill-parent
 * @description: UserEntity
 * @author: PlusL
 * @create: 2022-07-05 14:46
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Alias("User")
public class User implements Serializable {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long id;

    /**
     * 用户名/手机号
     */
    @NotNull(message = "用户昵称/手机号/账号不能为空")
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册时间
     */
    private Date registerDate;

    /**
     * 最后登录时间
     */
    private Date lastLoginDate;

    /**
     * 登录计数（未使用）
     */
    private Integer loginCount;
}
