package com.plusl.framework.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

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
public class User {

    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;

}
