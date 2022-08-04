package com.plusl.core.service;

import com.plusl.core.facade.api.entity.User;
import com.plusl.core.facade.api.entity.dto.UserLoginDTO;
import com.plusl.core.facade.api.entity.dto.UserWithTokenDTO;


/**
 * @program: seckill-parent
 * @description: 用户服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:14
 **/
public interface UserService {

    /**
     * 检查密码并登录
     *
     * @param userLoginDTO 用户登录传输类
     * @return 用户&token
     */
    UserWithTokenDTO checkPasswordAndLogin(UserLoginDTO userLoginDTO);

    /**
     * 创建token
     *
     * @param userLoginDTO 用户登录传输类
     * @return token
     */
    String createToken(UserLoginDTO userLoginDTO);

    /**
     * 通过token获取用户
     *
     * @param token token
     * @return 用户实体
     */
    User getUserByToken(String token);

    /**
     * 通过用户名/手机号获取用户
     *
     * @param nickName 用户名/手机号
     * @return 用户实体
     */
    User getUserByNickName(String nickName);
}
