package com.plusl.web.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.plusl.core.facade.api.entity.User;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.framework.common.enums.status.ResultStatus;
import com.plusl.web.utils.UserContext;
import com.plusl.framework.redis.RedisService;
import com.plusl.web.client.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

import static com.plusl.framework.common.constant.CommonConstant.COOKIE_NAME_TOKEN;
import static com.plusl.framework.redis.constant.RedisConstant.USER_EXPIRE_TIME;


/**
 * @author LJH
 * @discription 登录拦截器
 */
@Slf4j
@Service
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    UserClient userClient;

    @Autowired
    RedisService redisService;

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
                if (user == null) {
                    log.warn("当前未登录，请登录后重试");
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("方法 [loginInterceptor] 捕获异常 : ", e);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }

    private void render(HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(CommonResult.error(ResultStatus.SESSION_ERROR));
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
        if (!ObjectUtil.isNull(user)) {
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
