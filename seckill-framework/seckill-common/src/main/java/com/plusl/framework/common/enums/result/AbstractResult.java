package com.plusl.framework.common.enums.result;

import com.plusl.framework.common.enums.status.ResultStatus;


/**
 * @program: seckill-parent
 * @description: 抽象结果
 * @author: PlusL
 * @create: 2022-07-05 15:23
 **/
public class AbstractResult {
    private ResultStatus resultStatus;
    private int code;
    private String message;

    AbstractResult() {
    }

    protected AbstractResult(ResultStatus status, String message) {
        this.code = status.getCode();
        this.resultStatus = status;
        this.message = message;
    }

    protected AbstractResult(ResultStatus status) {
        this.code = status.getCode();
        this.resultStatus = status;
        this.message = status.getMessage();
    }

    public AbstractResult success() {
        this.resultStatus = ResultStatus.SUCCESS;
        return this;
    }

    public AbstractResult withError(ResultStatus status) {
        this.resultStatus = status;
        return this;
    }

    public AbstractResult withError(String message) {
        this.resultStatus = ResultStatus.SYSTEM_ERROR;
        this.message = message;
        return this;
    }

    public AbstractResult withError(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public ResultStatus getStatus() {
        return this.resultStatus;
    }

    public String getMessage() {
        return this.message == null ? this.resultStatus.getMessage() : this.message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
