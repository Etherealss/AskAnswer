package cn.hwb.common.security.token.user;

import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.common.security.auth.exception.TokenException;
import cn.hwb.common.security.config.UserCertificateConfig;
import cn.hwb.common.security.token.RedisTokenService;
import cn.hwb.common.security.token.TokenCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author hwb
 */
@SuppressWarnings("unchecked")
@Component
@Slf4j
public class UserTokenService extends RedisTokenService<UserTokenCertificate> {

    public UserTokenService(UserCertificateConfig config, RedisTemplate<String, TokenCertificate> redisTemplate) {
        super(config, redisTemplate);
    }

    @Override
    public UserTokenCertificate assertToken(String token) throws TokenException {
        TokenCertificate certificate = redisTemplate.opsForValue().get(tokenKey(token));
        if (certificate == null) {
            throw new TokenException(ResultCode.USER_TOKEN_INVALID);
        }
        return (UserTokenCertificate) certificate;
    }

    @Override
    public UserTokenCertificate getToken(String token) {
        TokenCertificate certificate = redisTemplate.opsForValue().get(tokenKey(token));
        if (certificate == null) {
            return null;
        }
        return (UserTokenCertificate) certificate;
    }
}
