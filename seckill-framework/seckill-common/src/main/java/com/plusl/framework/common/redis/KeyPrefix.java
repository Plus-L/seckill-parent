package com.plusl.framework.common.redis;

public interface KeyPrefix {



    public int expireSeconds();

    public String getPrefix();

}
