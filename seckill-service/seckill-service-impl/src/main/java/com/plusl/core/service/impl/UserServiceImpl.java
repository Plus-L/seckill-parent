package com.plusl.core.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.plusl.core.facade.api.entity.User;
import com.plusl.core.service.UserService;
import com.plusl.core.service.mapper.UserMapper;
import com.plusl.core.facade.api.entity.dto.UserLoginDTO;
import com.plusl.core.facade.api.entity.dto.UserWithTokenDTO;
import com.plusl.framework.common.exception.GlobalException;
import com.plusl.framework.common.utils.UUIDUtil;
import com.plusl.framework.redis.RedisKeyUtils;
import com.plusl.framework.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.plusl.framework.common.enums.status.ResultStatus.*;
import static com.plusl.framework.redis.constant.RedisConstant.USER_EXPIRE_TIME;

/**
 * @program: seckill-parent
 * @description: 用户业务处理
 * @author: PlusL
 * @create: 2022-07-06 09:01
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    @Override
    public UserWithTokenDTO checkPasswordAndLogin(UserLoginDTO userLoginDTO) {

        if (ObjectUtil.isEmpty(userLoginDTO)) {
            throw new GlobalException(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMessage());
        }

        String mobile = userLoginDTO.getNickname();
        String password = userLoginDTO.getPassword();
        User user = getUserByNickName(mobile);
        if (ObjectUtil.isEmpty(user)) {
            throw new GlobalException(MOBILE_NOT_EXIST.getCode(), MOBILE_NOT_EXIST.getMessage());
        }

        String dbPass = user.getPassword();
        if (!password.equals(dbPass)) {
            throw new GlobalException(PASSWORD_ERROR.getCode(), PASSWORD_ERROR.getMessage());
        }
        //生成cookie 将session返回游览器 分布式session
        String token = UUIDUtil.uuid();

        redisService.set(RedisKeyUtils.getPrefixUserToken(token), user, USER_EXPIRE_TIME);
        UserWithTokenDTO userWithTokenDTO = new UserWithTokenDTO();
        userWithTokenDTO.setUser(user);
        userWithTokenDTO.setToken(token);
        return userWithTokenDTO;
    }


    @Override
    public String createToken(UserLoginDTO userLoginDTO) {
        if (userLoginDTO == null) {
            throw new GlobalException(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMessage());
        }

        String mobile = userLoginDTO.getNickname();
        String password = userLoginDTO.getPassword();
        User user = getUserByNickName(mobile);
        if (user == null) {
            throw new GlobalException(MOBILE_NOT_EXIST.getCode(), MOBILE_NOT_EXIST.getMessage());
        }

        String dbPass = user.getPassword();
        if (!password.equals(dbPass)) {
            throw new GlobalException(PASSWORD_ERROR.getCode(), PASSWORD_ERROR.getMessage());
        }
        //生成cookie 将session返回游览器 分布式session
        String token = UUIDUtil.uuid();
        redisService.set(RedisKeyUtils.getPrefixUserToken(token), user, USER_EXPIRE_TIME);
        return token;
    }


    @Override
    public User getUserByToken(String token) {

        if (StringUtils.isEmpty(token)) {
            log.warn("方法 [getUserByToken] 传入token为空");
            return null;
        }
        User user = redisService.get(RedisKeyUtils.getPrefixUserToken(token), User.class);
        if (ObjectUtil.isEmpty(user)) {
            log.warn("方法 [getUserByToken] 通过token读Redis为空");
            return null;
        }
        return user;

    }

    @Override
    public User getUserByNickName(String nickName) {
        //取缓存
        User user = redisService.get(RedisKeyUtils.getPrefixUserByName(nickName), User.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = userMapper.getByNickname(nickName);
        if (user != null) {
            redisService.set(RedisKeyUtils.getPrefixUserByName(nickName), user, USER_EXPIRE_TIME);
        }
        return user;
    }


}
