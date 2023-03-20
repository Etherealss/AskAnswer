package cn.hwb.common.security.auth.exception;


import cn.hwb.common.base.enums.ResultCode;
import cn.hwb.common.base.exception.service.AuthenticationException;

/**
 * @author wtk
 * @description token异常
 * @date 2021-10-05
 */
public class TokenException extends AuthenticationException {
    public TokenException(ResultCode resultCode) {
        super(resultCode);
    }

    public TokenException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }
}
