package cn.hwb.common.security.auth.exception;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.service.AuthenticationException;

/**
 * @author hwb
 * @description token异常
 
 */
public class TokenException extends AuthenticationException {
    public TokenException(ResultCode resultCode) {
        super(resultCode);
    }

    public TokenException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }
}
