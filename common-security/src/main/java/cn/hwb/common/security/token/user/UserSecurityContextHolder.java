package cn.hwb.common.security.token.user;

import cn.hutool.extra.spring.SpringUtil;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.utils.ServletUtil;
import cn.hwb.common.security.auth.exception.TokenException;
import cn.hwb.common.security.config.UserCertificateConfig;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 暂存当前线程中的用户权限信息
 * @author wtk
 * @date 2022-08-30
 */
public class UserSecurityContextHolder {
    private static final ThreadLocal<UserTokenCertificate> USER_CREDENTIALS = new InheritableThreadLocal<>();
    private static final UserCertificateConfig TOKEN_CONFIG = SpringUtil.getBean(UserCertificateConfig.class);

    public static void set(UserTokenCertificate userCredential) {
        Objects.requireNonNull(userCredential.getUserId(), "Token中不包含UserId");
        Objects.requireNonNull(userCredential.getUsername(), "Token中不包含Username");
        Objects.requireNonNull(userCredential.getToken(), "Token中不包含Token字符串");
        Objects.requireNonNull(userCredential.getBirthday(), "Token中不包含生日");
        Objects.requireNonNull(userCredential.getAgeBracket(), "Token中不包含年龄段");
        Objects.requireNonNull(userCredential.getRoles(), "Token中不包含角色");
        USER_CREDENTIALS.set(userCredential);
    }

    @Nullable
    public static UserTokenCertificate get() {
        return USER_CREDENTIALS.get();
    }

    @NonNull
    public static UserTokenCertificate require() {
        UserTokenCertificate userCredential = USER_CREDENTIALS.get();
        if (userCredential == null) {
            String token = ServletUtil.getRequest().getHeader(TOKEN_CONFIG.getHeaderName());
            if (!StringUtils.hasText(token)) {
                throw new TokenException(ResultCode.USER_TOKEN_MISSING);
            } else {
                throw new TokenException(ResultCode.USER_TOKEN_INVALID);
            }
        }
        return userCredential;
    }

    public static void remove() {
        USER_CREDENTIALS.remove();
    }


}
