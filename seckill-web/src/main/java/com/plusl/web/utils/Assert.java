package com.plusl.web.utils;

import com.google.common.base.Objects;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.exception.GlobalException;

import static com.plusl.framework.common.enums.status.ResultStatus.SUCCESS;

/**
 * @program: seckill-framework
 * @description:
 * @author: PlusL
 * @create: 2022-07-23 17:46
 **/
public class Assert {

    public static void isSuccess(FacadeResult<?> result) {
        if (!Objects.equal(result.getErrorCode(), SUCCESS.getCode())) {
            throw new GlobalException(result.getErrorCode(), result.getMessage());
        }
    }

    public static void isSuccess(FacadeResult<?> result, String message) {
        if (!Objects.equal(result.getErrorCode(), SUCCESS.getCode())) {
            throw new GlobalException(message);
        }
    }

}
