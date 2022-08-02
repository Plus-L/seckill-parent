package com.plusl.core.facade.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: seckill-framework
 * @description: 统一Facade层返回格式
 * @author: PlusL
 * @create: 2022-07-23 15:33
 **/

@Data
@NoArgsConstructor
public class FacadeResult<T> implements Serializable {

    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 信息
     */
    private String message;
    /**
     * 导致原因
     */
    private String cause;
    private T data;

    private FacadeResult(String errorCode, String message, String cause, T data) {
        this.errorCode = errorCode;
        this.message = message;
        this.cause = cause;
        this.data = data;
    }

    public static <T> FacadeResult<T> success(T data) {
        return new FacadeResult<>("200", "成功", null, data);
    }

    public static <T> FacadeResult<T> success() {
        return new FacadeResult<>("200", "成功", null, null);
    }

    public static <T> FacadeResult<T> fail(String errorCode, String message) {
        return new FacadeResult<>(errorCode, message, null, null);
    }

    public static <T> FacadeResult<T> fail(String errorCode, String message, String cause) {
        return new FacadeResult<>(errorCode, message, cause, null);
    }

    public static <T> FacadeResult<T> fail(String message, T data) {
        return new FacadeResult<>("400", message, null, data);
    }

    public static <T> FacadeResult<T> fail(Exception e) {
        return new FacadeResult<>("400", "失败", e.getMessage(), null);
    }


}
