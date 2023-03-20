package cn.hwb.common.security.token;

import cn.hwb.common.security.config.UserCertificateConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author wtk
 * @date 2022-08-30
 */
@SuppressWarnings("unchecked")
@Component
@Slf4j
@RequiredArgsConstructor
public abstract class RedisTokenService<T extends TokenCertificate> implements ITokenService<T> {

    protected final UserCertificateConfig config;
    protected final RedisTemplate<String, TokenCertificate> redisTemplate;

    @Override
    public void createToken(TokenCertificate certificate) {
        UUID token = UUID.randomUUID();
        certificate.setToken(token.toString());
        String redisKey = tokenKey(certificate.getToken());
        redisTemplate.opsForValue().set(redisKey, certificate);
        Date tokenExpireAt = new Date(config.getExpireMs() + System.currentTimeMillis());
        redisTemplate.expireAt(redisKey, tokenExpireAt);
    }

    protected String tokenKey(String token) {
        return config.getCacheKey() + ":" + token;
    }
}
