package com.plusl.common.enums.result;

import com.plusl.common.enums.status.ResultStatus;

import java.io.Serializable;

/**
 * @program: seckill-parent
 * @description: 规范返回结果
 * @author: PlusL
 * @create: 2022-07-05 15:36
 **/
public class Result<T> extends AbstractResult implements Serializable {

    private T data;
    private Integer count;

    protected Result() {
    }

    protected Result(ResultStatus status, String message) {
        super(status, message);
    }

    protected Result(ResultStatus status) {
        super(status);
    }

    public static <T> Result<T> build() {
        return new Result(ResultStatus.SUCCESS, (String) null);
    }

    public static <T> Result<T> success(String message) {
        return new Result(ResultStatus.SUCCESS, message);
    }

    public static <T> Result<T> error(ResultStatus status) {
        return new Result<T>(status);
    }

    public static <T> Result<T> error(ResultStatus status, String message) {
        return new Result<T>(status, message);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void success(T value) {
        this.success();
        this.data = value;
        this.count = 0;
    }


}
