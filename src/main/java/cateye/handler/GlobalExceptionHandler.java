package cateye.handler;

import cateye.response.HttpResult;
import cateye.response.ResultResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static int DUPLICATE_KEY_CODE = 1001;
    private static int PARAM_FAIL_CODE = 1002;
    private static int VALIDATION_CODE = 1003;


    /**
     * 方法参数校验
     */
    @ExceptionHandler(BindException.class)
    public HttpResult handleBindException(BindException e) {
        return ResultResponse.failure(PARAM_FAIL_CODE, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public HttpResult handleValidationException(ValidationException e) {
        return ResultResponse.failure(VALIDATION_CODE, e.getCause().getMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public HttpResult handleConstraintViolationException(ConstraintViolationException e) {
        return ResultResponse.failure(PARAM_FAIL_CODE, e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public HttpResult handlerNoFoundException(Exception e) {
        return ResultResponse.failure(404, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public HttpResult handleDuplicateKeyException(DuplicateKeyException e) {
        return ResultResponse.failure(DUPLICATE_KEY_CODE, "数据重复，请检查后提交");
    }


    @ExceptionHandler(Exception.class)
    public HttpResult handleException(Exception e) {
        return ResultResponse.failure(500, "系统繁忙,请稍后再试");
    }

}
