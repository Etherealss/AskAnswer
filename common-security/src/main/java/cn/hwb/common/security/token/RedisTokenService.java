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

    /**
     * 完善 TokenCertificate 的信息，并存储到Redis上
     * @param certificate
     */
    @Override
    public void completeTokenAndSave(TokenCertificate certificate) {
        Objects.requireNonNull(certificate, "TokenCertificate 不能为空");

        // 设置token字符串
        UUID token = UUID.randomUUID();
        certificate.setToken(token.toString());

        // 设置过期时间
        Date tokenExpireAt = new Date(config.getExpireMs() + System.currentTimeMillis());
        certificate.setExpiryDate(tokenExpireAt);

        // 保存token到Redis
        String redisKey = tokenKey(certificate.getToken());
        redisTemplate.opsForValue().set(redisKey, certificate);
        // redis的 set 方法只支持设置时间段（例如10分钟后过期），想要设置到期日期的话只能用 expireAt 方法
        redisTemplate.expireAt(redisKey, tokenExpireAt);
    }

    /**
     * 使TokenCertificate失效
     * @param token
     */
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
