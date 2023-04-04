package cn.hwb.askanswer.user.infrastructure.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@ConfigurationProperties("app.user.avatar.oss")
public class UserAvatarProperties {
    @NotBlank
    String dir;
    @NotBlank
    String defaultAvatarKey;
}
