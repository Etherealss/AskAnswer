package cn.hwb.common.security.auth.exception;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.service.AuthenticationException;

/**
 * token异常
 * @author hwb
 */
public class TokenException extends AuthenticationException {
    public TokenException(ResultCode resultCode) {
        super(resultCode);
    }

    public TokenException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }
}
