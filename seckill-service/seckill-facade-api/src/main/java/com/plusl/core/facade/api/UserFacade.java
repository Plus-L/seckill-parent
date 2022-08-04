package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.User;
import com.plusl.core.facade.api.entity.dto.UserLoginDTO;
import com.plusl.core.facade.api.entity.dto.UserWithTokenDTO;


/**
 * @program: seckill-framework
 * @description: 用户Facade层
 * @author: PlusL
 * @create: 2022-07-23 15:29
 **/
public interface UserFacade {

    /**
     * 验证密码并登录
     *
     * @param userLoginDTO 用户登录DTO
     * @return FacadeResult封装用户+token DTO
     */
    FacadeResult<UserWithTokenDTO> checkPasswordAndLogin(UserLoginDTO userLoginDTO);

    /**
     * 创建token
     *
     * @param userLoginDTO 用户登录DTO
     * @return token
     */
    FacadeResult<String> createToken(UserLoginDTO userLoginDTO);

    /**
     * 通过token获取用户
     *
     * @param token token
     * @return 用户实体
     */
    FacadeResult<User> getUserByToken(String token);

    /**
     * 通过用户名获取用户
     *
     * @param nickName 用户名/手机号
     * @return 用户实体
     */
    FacadeResult<User> getUserByNickName(String nickName);

}
