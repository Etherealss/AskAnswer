package cn.hwb.common.security.token;

import cn.hwb.common.security.config.UserCertificateConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author hwb
 */
@SuppressWarnings("unchecked")
@Component
@Slf4j
@RequiredArgsConstructor
public abstract class RedisTokenService<T extends TokenCertificate> implements ITokenService<T> {

    protected final UserCertificateConfig config;
    protected final RedisTemplate<String, TokenCertificate> redisTemplate;

    @Override
    public void completeTokenAndSave(TokenCertificate certificate) {
        Objects.requireNonNull(certificate, "TokenCertificate 不能为空");
        UUID token = UUID.randomUUID();
        certificate.setToken(token.toString());
        String redisKey = tokenKey(certificate.getToken());
        redisTemplate.opsForValue().set(redisKey, certificate);
        Date tokenExpireAt = new Date(config.getExpireMs() + System.currentTimeMillis());
        redisTemplate.expireAt(redisKey, tokenExpireAt);
        certificate.setExpiryDate(tokenExpireAt);
    }

    @Override
    public void invalidateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("Token 不能为空");
        }
        String tokenKey = tokenKey(token);
        redisTemplate.opsForValue().getAndDelete(tokenKey);
    }

    protected String tokenKey(String token) {
        return config.getCacheKey() + ":" + token;
    }
}
