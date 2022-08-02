package com.plusl.core.service;

import com.plusl.framework.common.dto.UserLoginDTO;
import com.plusl.framework.common.dto.UserWithTokenDTO;
import com.plusl.framework.common.entity.User;

/**
 * @program: seckill-parent
 * @description: 用户服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:14
 **/
public interface UserService {

    UserWithTokenDTO checkPasswordAndLogin(UserLoginDTO userLoginDTO);

    String createToken(UserLoginDTO userLoginDTO);

    User getUserByToken(String token);

    User getUserByNickName(String nickName);
}
