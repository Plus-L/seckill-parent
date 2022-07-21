package com.plusl.framework.common.exception;


/**
 * @program: seckill-parent
 * @description: 常规异常类
 * @author: PlusL
 * @create: 2022-07-06 09:27
 **/

import lombok.Getter;

/**
 * @program: seckill-parent
 * @description: 常规异常类
 * @author: PlusL
 * @create: 2022-07-06 09:27
 **/
public class GlobalException extends RuntimeException {

    private static final Integer FAIL_CODE = 400;

    @Getter
    private final Integer code;

    @Getter
    private final String errorMessage;

    public GlobalException() {
        this(FAIL_CODE, null);
    }

    public GlobalException(Integer code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public GlobalException(String errorMessage) {
        this(FAIL_CODE, errorMessage);
    }

    public GlobalException(Throwable cause) {
        this(FAIL_CODE, null, cause);
    }

    public GlobalException(Throwable cause, String errorMessage){
        this(FAIL_CODE, errorMessage, cause);
    }

    public GlobalException(Integer code, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
