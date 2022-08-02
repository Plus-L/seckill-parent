package com.plusl.web.interceptor;

import com.plusl.framework.common.enums.result.Result;
import com.plusl.framework.common.exception.GlobalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.plusl.framework.common.enums.status.ResultStatus.SESSION_ERROR;
import static com.plusl.framework.common.enums.status.ResultStatus.SYSTEM_ERROR;

/**
 * @program: seckill-parent
 * @description: 全局异常管理捕获, 通过@ExceptionHandler的value指定需要拦截的异常类型
 * @author: PlusL
 * @create: 2022-07-06 18:05
 **/

@Deprecated
//@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            //TODO: 统一返回类待处理 // 已停用当前handler
            return Result.error(SYSTEM_ERROR);
        } else if (e instanceof org.springframework.validation.BindException) {
            org.springframework.validation.BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            assert msg != null;
            //打印堆栈信息
            logger.error(String.format(msg, msg));
            return Result.error(SESSION_ERROR);
        } else {
            return Result.error(SYSTEM_ERROR);
        }
    }
}
