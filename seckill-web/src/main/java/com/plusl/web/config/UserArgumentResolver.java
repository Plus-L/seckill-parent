package com.plusl.web.config;

import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.utils.UserContext;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author LJH
 * @discription 参数解析器，supportsParameter对参数进行过滤，只有通过了过滤的参数才会进入到resolveArgument中执行后续处理
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 条件判断，当supportParameter返回值为true时，调用resolveArgument方法
     *
     * @param methodParameter 方法体
     * @return true/false
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory webDataBinderFactory)
            throws Exception {
        /**
         *  threadlocal 存储线程副本 保证线程不冲突
         */
        return UserContext.getUser();
    }

}
