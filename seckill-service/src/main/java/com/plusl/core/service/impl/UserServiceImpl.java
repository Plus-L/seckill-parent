package com.plusl.core.service.impl;


import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.exception.GlobalException;
import com.plusl.framework.common.redis.RedisUtil;
import com.plusl.framework.common.redis.UserKey;
import com.plusl.framework.common.utils.UUIDUtil;
import com.plusl.framework.common.vo.LoginVo;
import com.plusl.core.service.Interface.UserService;
import com.plusl.core.service.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.plusl.framework.common.enums.status.ResultStatus.*;

/**
 * @program: seckill-parent
 * @description: 用户业务处理
 * @author: PlusL
 * @create: 2022-07-06 09:01
 **/
@Service
@DubboService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    public static final String COOKIE_NAME_TOKEN = "token";
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMessage());
        }

        String mobile = loginVo.getNickname();
        String password = loginVo.getPassword();
        User user = getByNickName(mobile);
        if (user == null) {
            throw new GlobalException(MOBILE_NOT_EXIST.getCode(), MOBILE_NOT_EXIST.getMessage());
        }

        String dbPass = user.getPassword();
        if (!password.equals(dbPass)) {
            throw new GlobalException(PASSWORD_ERROR.getCode(), PASSWORD_ERROR.getMessage());
        }
        //生成cookie 将session返回游览器 分布式session
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }


    @Override
    public String createToken(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(SYSTEM_ERROR.getCode(),SYSTEM_ERROR.getMessage());
        }

        String mobile = loginVo.getNickname();
        String password = loginVo.getPassword();
        User user = getByNickName(mobile);
        if (user == null) {
            throw new GlobalException(MOBILE_NOT_EXIST.getCode(), MOBILE_NOT_EXIST.getMessage());
        }

        String dbPass = user.getPassword();
        if (!password.equals(dbPass)) {
            throw new GlobalException(PASSWORD_ERROR.getCode(), PASSWORD_ERROR.getMessage());
        }
        //生成cookie 将session返回游览器 分布式session
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisUtil.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置有效期
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    @Override
    public User getByToken(HttpServletResponse response, String token) {

        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisUtil.get(UserKey.token, token, User.class);
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;

    }

    @Override
    public User getByNickName(String nickName) {
        //取缓存
        User user = redisUtil.get(UserKey.getByNickName, "" + nickName, User.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = userMapper.getByNickname(nickName);
        if (user != null) {
            redisUtil.set(UserKey.getByNickName, "" + nickName, user);
        }
        return user;
    }


}
