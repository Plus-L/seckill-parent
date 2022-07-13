package com.plusl.service.redis;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();

}
