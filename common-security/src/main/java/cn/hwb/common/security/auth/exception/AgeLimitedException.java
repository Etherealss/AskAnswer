package cn.hwb.common.security.auth.exception;

import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.service.AuthenticationException;

/**
 * @author hwb
 */
public class AgeLimitedException extends AuthenticationException {
    public AgeLimitedException(String message) {
        super(ResultCode.AGE_NOT_ALLOW, message);
    }

    public AgeLimitedException() {
        super(ResultCode.AGE_NOT_ALLOW);
    }
}
