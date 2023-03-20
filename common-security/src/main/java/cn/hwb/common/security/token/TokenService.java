package cn.hwb.common.security.token;

import cn.hwb.common.security.config.UserCertificateConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wtk
 * @date 2022-10-09
 */
@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final RedisTemplate<String, TokenCertificate> redisTemplate;
    private final UserCertificateConfig userCertificateConfig;

    public <T extends TokenCertificate> T verifyAndGet(String token) {
        // TODO Cacheable
        String key = userCertificateConfig.getCacheKey() + ":" + token;
        TokenCertificate tokenCertificate = redisTemplate.opsForValue().get(key);
        if (tokenCertificate != null) {
            return (T) tokenCertificate;
        }
        // TODO 验证Token
        // cache(key, tokenCertificate, tokenCertificate.getExpiryDate());
        return (T) tokenCertificate;
    }

    private void cache(String key, TokenCertificate value, Date expireAt) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expireAt(key, expireAt);
    }

    public <T> void invalidCache(String token, Class<T> credentialType) {
        String key = userCertificateConfig.getCacheKey() + ":" + token;
        redisTemplate.opsForValue().getAndDelete(key);
    }
}
