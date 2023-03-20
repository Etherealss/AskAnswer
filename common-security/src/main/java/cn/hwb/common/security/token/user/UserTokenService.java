package cn.hwb.common.security.token.user;

import cn.hwb.common.base.enums.ResultCode;
import cn.hwb.common.security.auth.exception.TokenException;
import cn.hwb.common.security.config.UserCertificateConfig;
import cn.hwb.common.security.token.RedisTokenService;
import cn.hwb.common.security.token.TokenCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author wtk
 * @date 2023-03-20
 */
@SuppressWarnings("unchecked")
@Component
@Slf4j
public class UserTokenService extends RedisTokenService<UserTokenCertificate> {

    public UserTokenService(UserCertificateConfig config, RedisTemplate<String, TokenCertificate> redisTemplate) {
        super(config, redisTemplate);
    }

    @Override
    public UserTokenCertificate verifyToken(String token) {
        TokenCertificate certificate = redisTemplate.opsForValue().get(tokenKey(token));
        if (certificate == null) {
            throw new TokenException(ResultCode.USER_TOKEN_INVALID);
        }
        return (UserTokenCertificate) certificate;
    }

    @Override
    public void invalidateToken(String token) {
        // 来到这里，可以认为 token 有效
        String tokenKey = tokenKey(token);
        redisTemplate.opsForValue().getAndDelete(tokenKey);
    }
}
