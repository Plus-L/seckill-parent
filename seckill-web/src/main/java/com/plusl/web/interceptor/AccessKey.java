package com.plusl.web.interceptor;

import com.plusl.framework.common.redis.BasePrefix;

public class AccessKey extends BasePrefix {

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "interceptor");
    }

}
