package com.plusl.core.facade.impl;

import cn.hutool.core.util.ObjectUtil;
import com.plusl.core.facade.api.UserFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.service.UserService;
import com.plusl.framework.common.dto.UserLoginDTO;
import com.plusl.framework.common.dto.UserWithTokenDTO;
import com.plusl.framework.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.plusl.framework.common.enums.status.ResultStatus.*;

/**
 * @program: seckill-framework
 * @description: 用户Facade接口实现类
 * @author: PlusL
 * @create: 2022-07-25 14:34
 **/
@Slf4j
@Component
@DubboService(version = "1.0.0")
public class UserFacadeImpl implements UserFacade {

    @Autowired
    UserService userService;

    @Override
    public FacadeResult<UserWithTokenDTO> checkPasswordAndLogin(UserLoginDTO userLoginDTO) {

        try {
            UserWithTokenDTO userWithTokenDTO = userService.checkPasswordAndLogin(userLoginDTO);
            if (ObjectUtil.isEmpty(userWithTokenDTO)) {
                return FacadeResult.fail(NICKNAME_OR_PASSWORD_ERROR.getCode(), NICKNAME_OR_PASSWORD_ERROR.getMessage());
            }
            return FacadeResult.success(userWithTokenDTO);
        } catch (Exception e) {
            log.warn("方法 [checkPasswordAndLogin] 异常 异常信息：", e);
            return FacadeResult.fail(NICKNAME_OR_PASSWORD_ERROR.getCode(), NICKNAME_OR_PASSWORD_ERROR.getMessage());
        }
    }

    @Override
    public FacadeResult<String> createToken(UserLoginDTO userLoginDTO) {

        try {
            String token = userService.createToken(userLoginDTO);
            if (StringUtils.isBlank(token)) {
                return FacadeResult.fail(NICKNAME_OR_PASSWORD_ERROR.getCode(), NICKNAME_OR_PASSWORD_ERROR.getMessage());
            }
            return FacadeResult.success(token);
        } catch (Exception e) {
            log.warn("方法 [checkPasswordAndLogin] 异常 异常信息：", e);
            return FacadeResult.fail(NICKNAME_OR_PASSWORD_ERROR.getCode(), NICKNAME_OR_PASSWORD_ERROR.getMessage());
        }
    }

    @Override
    public FacadeResult<User> getUserByToken(String token) {

        try {
            User user = userService.getUserByToken(token);
            if (ObjectUtil.isEmpty(user)) {
                return FacadeResult.fail(TOKEN_ERROR.getCode(), TOKEN_ERROR.getMessage());
            }
            return FacadeResult.success(user);
        } catch (Exception e) {
            log.warn("方法 [getUserByToken] 异常 异常信息：", e);
            return FacadeResult.fail(TOKEN_ERROR.getCode(), TOKEN_ERROR.getMessage());
        }
    }

    @Override
    public FacadeResult<User> getUserByNickName(String nickName) {
        try {
            User user = userService.getUserByNickName(nickName);
            if (ObjectUtil.isEmpty(user)) {
                return FacadeResult.fail(USER_NOT_EXIST.getCode(), USER_NOT_EXIST.getMessage());
            }
            return FacadeResult.success(user);
        } catch (Exception e) {
            log.warn("方法 [getUserByNickName] 异常 异常信息：", e);
            return FacadeResult.fail(USER_NOT_EXIST.getCode(), USER_NOT_EXIST.getMessage());
        }
    }
}
