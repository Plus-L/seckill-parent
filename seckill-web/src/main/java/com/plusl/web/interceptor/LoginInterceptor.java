package com.plusl.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.enums.result.Result;
import com.plusl.framework.common.enums.status.ResultStatus;
import com.plusl.framework.common.redis.RedisUtil;
import com.plusl.framework.common.utils.UserContext;
import com.plusl.core.service.Interface.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

import static com.plusl.framework.common.enums.status.ResultStatus.ACCESS_LIMIT_REACHED;
import static com.plusl.framework.common.enums.status.ResultStatus.SESSION_ERROR;


/**
 * @author LJH
 * @discription 登录拦截器
 */
@Service
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        /**
         * 获取调用 获取主要方法
         */
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod hm = (HandlerMethod) handler;
                User user = getUser(request, response);
                UserContext.setUser(user);
                RequireLogin accessLimit = hm.getMethodAnnotation(RequireLogin.class);
                if (accessLimit == null) {
                    return true;
                }
                int seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();
                boolean needLogin = accessLimit.needLogin();
                String key = request.getRequestURI();
                if (needLogin) {
                    if (user == null) {
                        render(response, SESSION_ERROR);
                        return false;
                    }
                    key += "_" + user.getNickname();
                } else {
                    //do nothing
                }
                AccessKey ak = AccessKey.withExpire(seconds);
                Integer count = redisUtil.get(ak, key, Integer.class);
                if (count == null) {
                    redisUtil.set(ak, key, 1);
                } else if (count < maxCount) {
                    redisUtil.incr(ak, key);
                } else {
                    render(response, ACCESS_LIMIT_REACHED);
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("loginInterceptor主要方法异常", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }

    private void render(HttpServletResponse response, ResultStatus cm) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter("TOKEN");
        String cookieToken = getCookieValue(request, "TOKEN");
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
