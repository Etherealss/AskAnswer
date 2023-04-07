package cn.hwb.askanswer.user.infrastructure.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

/**
 * @author hwb
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
