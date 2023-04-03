package cn.hwb.askanswer.common.base.web;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.rest.ParamErrorException;
import cn.hwb.askanswer.common.base.exception.service.AuthenticationException;
import cn.hwb.askanswer.common.base.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;

/**
 * @author wtk
 * @version 2.0 将多个异常合并
 * @description 全局异常处理
 * @date 2021-08-13
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            UnsupportedOperationException.class,
    })
    public Result<Void> handle(UnsupportedOperationException e) {
        log.info("不支持的操作：" + e.getMessage());
        return new Result<>(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            BadRequestException.class,
    })
    public Result<Void> handle(BadRequestException e) {
        log.info("用户请求存在问题：", e);
        return new Result<>(e);
    }

    /**
     * 前后端交接的接口参数缺失
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Object> handle(MissingServletRequestParameterException e) {
        log.warn("前后端交接的接口参数缺失:" + e.getMessage());
        return new Result<>(false, ResultCode.ERROR_PARAM, "前后端交接的接口参数缺失");
    }

    /**
     * 参数不可读
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handle(HttpMessageNotReadableException e) {
        log.warn("参数不可读异常", e);
        return new Result<>(false, ResultCode.ERROR_PARAM, "参数不可读，请检查参数列表是否完整：" + e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handle(MethodArgumentTypeMismatchException e) {
        // 可能会收到 EnumIllegalException
        Throwable throwable = e;
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        if (throwable instanceof BadRequestException) {
            return handle((BadRequestException) throwable);
        } else {
            return new Result<>(false, ResultCode.OPERATE_UNSUPPORTED, "方法参数不匹配：" + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handle(HttpRequestMethodNotSupportedException e) {
        return new Result<>(false, ResultCode.OPERATE_UNSUPPORTED, "请求方式不支持：" + e.getMessage());
    }

    /**
     * 无权访问
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handle(AccessDeniedException e) {
        log.info("[无权访问]" + e.getMessage());
        return new Result<>(false, ResultCode.FORBIDDEN_REQUEST);
    }

    /**
     * 包装绑定异常结果
     */
    @ExceptionHandler(BindException.class)
    private Result<Void> wrapperBindingResult(BindException bindException) {
        BindingResult bindingResult = bindException.getBindingResult();
        StringBuilder msg = new StringBuilder();

        for (ObjectError error : bindingResult.getAllErrors()) {
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
            msg.append(", ");

        }
        String s = msg.delete(msg.length() - 2, msg.length()).toString();
        log.info("参数格式验证不通过：{}", s);
        return new Result<>(false, ResultCode.ERROR_PARAM, s);
    }

    /**
     * 验证异常处理
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handle(ConstraintViolationException ex, WebRequest request) {
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            msg.append(constraintViolation.getPropertyPath()).append(": ");
            msg.append(constraintViolation.getMessage() == null ? "" : constraintViolation.getMessage());
            msg.append(", ");
        }
        String s = msg.delete(msg.length() - 2, msg.length()).toString();
        log.info("参数格式验证不通过：{}", s);
        return new Result<>(false, ResultCode.ERROR_PARAM, s);
    }

    /**
     * 权限认证异常
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException.class)
    public Result<Object> handle(AuthenticationException e) {
        log.info("权限认证异常: {}", e.getMessage());
        return new Result<>(e);
    }

    /**
     * 后端有bug
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public Result<Object> handle(RuntimeException e) {
        log.warn("后端 RuntimeException", e);
        return new Result<>(e);
    }

    /**
     * 其他未处理异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<Object> handle(Exception e) {
        if (e instanceof IllegalStateException) {
            if (e.getMessage().contains("argument type mismatch\nController")) {
                log.warn("参数类型错误:", e);
                return new Result<>(new ParamErrorException("参数类型错误：" + e.getMessage()));
            }
        }
        log.warn("[全局异常处理器]其他异常:", e);
        return new Result<>(e);
    }
}
