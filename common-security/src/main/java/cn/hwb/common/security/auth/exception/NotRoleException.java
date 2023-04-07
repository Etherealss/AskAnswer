package cn.hwb.common.security.auth.exception;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.service.AuthenticationException;

/**
 * @author hwb
 */
public class NotRoleException extends AuthenticationException {
    public NotRoleException(ResultCode resultCode) {
        super(resultCode);
    }

    public NotRoleException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }

    public NotRoleException(String... roles) {
        this(ResultCode.ROLE_NOT_MATCH, "缺少角色身份：" + String.join(",", roles));
    }
}
