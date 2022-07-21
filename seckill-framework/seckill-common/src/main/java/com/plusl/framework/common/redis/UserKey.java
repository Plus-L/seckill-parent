package com.plusl.framework.common.redis;

public class UserKey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
    public static final int USER_EXPIRE = 3600 * 24 * 2;
    public static UserKey token = new UserKey(TOKEN_EXPIRE, "tk:");
    public static UserKey getByNickName = new UserKey(0, "userName:");

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
