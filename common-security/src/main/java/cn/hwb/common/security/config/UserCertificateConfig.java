package cn.hwb.common.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 1. 与用户鉴权相关的配置
 * 2. 各个服务验证 userToken 时，对结果的缓存的相关配置。缓存时长由 Credential 决定
 * @author hwb
 */
@Configuration
@ConfigurationProperties("app.token.user")
@Validated
@Getter
@Setter
public class UserCertificateConfig {
    /**
     * token放在header中的key值
     */
    @NotEmpty
    private String headerName;

    /**
     * redis 存储的 key（其实是一个前缀）
     */
    @NotEmpty
    private String cacheKey;

    /**
     * redis 存储的过期时间（单位：ms）
     */
    @NotNull
    private Long expireMs;
}
