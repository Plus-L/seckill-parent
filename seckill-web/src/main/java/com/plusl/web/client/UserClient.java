package com.plusl.web.client;

import com.plusl.core.facade.api.UserFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.dto.UserLoginDTO;
import com.plusl.framework.common.dto.UserWithTokenDTO;
import com.plusl.framework.common.entity.User;
import com.plusl.web.utils.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @program: seckill-framework
 * @description: 用户Client
 * @author: PlusL
 * @create: 2022-07-25 14:54
 **/

@Service
public class UserClient {

    @DubboReference(version = "1.0.0", timeout = 30000, check = false)
    UserFacade userFacade;

    public UserWithTokenDTO checkPasswordAndLogin(UserLoginDTO userLoginDTO) {
        FacadeResult<UserWithTokenDTO> result = userFacade.checkPasswordAndLogin(userLoginDTO);
        Assert.isSuccess(result);
        return result.getData();
    }

    public String createToken(UserLoginDTO userLoginDTO) {
        FacadeResult<String> result = userFacade.createToken(userLoginDTO);
        Assert.isSuccess(result);
        return result.getData();
    }

    public User getUserByToken(String token) {
        FacadeResult<User> result = userFacade.getUserByToken(token);
        Assert.isSuccess(result);
        return result.getData();
    }

    public User getUserByNickName(String nickName) {
        FacadeResult<User> result = userFacade.getUserByNickName(nickName);
        Assert.isSuccess(result);
        return result.getData();
    }

}
