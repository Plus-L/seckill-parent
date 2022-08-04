package com.plusl.web.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author LJH
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface RequireLogin {

}
