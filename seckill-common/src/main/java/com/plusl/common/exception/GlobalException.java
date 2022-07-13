package com.plusl.common.exception;

import com.plusl.common.enums.status.ResultStatus;

/**
 * @program: seckill-parent
 * @description: 常规异常类
 * @author: PlusL
 * @create: 2022-07-06 09:27
 **/
public class GlobalException extends RuntimeException {

    private ResultStatus status;

    public GlobalException(ResultStatus status) {
        super();
        this.status = status;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
