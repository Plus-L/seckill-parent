package com.plusl.web.utils;


import com.plusl.core.facade.api.entity.User;

/**
 * @author LJH
 * @discription 获取ThreadLocal线程副本，保证线程不冲突
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

    public static User getUser() {

        return userHolder.get();
    }

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static void removeUser() {
        userHolder.remove();
    }

}
