package cn.hwb.askanswer.common.base.exception.service;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * @author wtk
 * @date 2022-08-30
 */
public class AuthenticationException extends BaseException {
    public AuthenticationException(ResultCode resultCode) {
        super(resultCode);
    }

    public AuthenticationException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }

    public AuthenticationException(ResultCode resultCode, String message, Throwable e) {
        super(resultCode, message, e);
    }

    public AuthenticationException(int code, HttpStatus httpStatus) {
        super(code, httpStatus);
    }
}
