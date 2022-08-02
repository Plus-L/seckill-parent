package com.plusl.web.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.framework.common.enums.status.ResultStatus;
import com.plusl.framework.common.utils.UserContext;
import com.plusl.framework.redis.RedisUtil;
import com.plusl.web.client.UserClient;
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

import static com.plusl.framework.common.constant.CommonConstant.COOKIE_NAME_TOKEN;
import static com.plusl.framework.common.enums.status.ResultStatus.ACCESS_LIMIT_REACHED;
import static com.plusl.framework.common.enums.status.ResultStatus.SESSION_ERROR;
import static com.plusl.framework.redis.constant.RedisConstant.USER_EXPIRE_TIME;


/**
 * @author LJH
 * @discription 登录拦截器
 */
@Service
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    UserClient userClient;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod hm = (HandlerMethod) handler;
                User user = getUser(request, response);
                UserContext.setUser(user);
                RequireLogin accessLimit = hm.getMethodAnnotation(RequireLogin.class);
                if (accessLimit == null) {
                    return true;
                }
                Long seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();
                boolean needLogin = accessLimit.needLogin();
                String key = request.getRequestURI();
                if (needLogin) {
                    if (user == null) {
                        render(response, SESSION_ERROR);
                        return false;
                    }
                    key += "_" + user.getNickname();
                }
                Integer count = redisUtil.get(key, Integer.class);
                if (count == null) {
                    redisUtil.set(key, 1, seconds);
                } else if (count < maxCount) {
                    redisUtil.incr(key);
                } else {
                    render(response, ACCESS_LIMIT_REACHED);
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("方法 [loginInterceptor] 捕获异常 : ", e);
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
        String str = JSON.toJSONString(CommonResult.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userClient.getUserByToken(token);
        if (!ObjectUtil.isEmpty(user)) {
            Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
            //设置有效期
            cookie.setMaxAge(USER_EXPIRE_TIME.intValue());
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return user;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
