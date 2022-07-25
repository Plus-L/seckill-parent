package com.plusl.core.facade.api;

import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.dto.UserLoginDTO;
import com.plusl.framework.common.dto.UserWithTokenDTO;
import com.plusl.framework.common.entity.User;


/**
 * @program: seckill-framework
 * @description: 用户Facade层
 * @author: PlusL
 * @create: 2022-07-23 15:29
 **/
public interface UserFacade {

    FacadeResult<UserWithTokenDTO> checkPasswordAndLogin(UserLoginDTO userLoginDTO);

    FacadeResult<String> createToken(UserLoginDTO userLoginDTO);

    FacadeResult<User> getUserByToken(String token);

    FacadeResult<User> getUserByNickName(String nickName);

}
