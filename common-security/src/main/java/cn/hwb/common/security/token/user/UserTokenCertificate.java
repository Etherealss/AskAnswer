package cn.hwb.common.security.token.user;

import cn.hwb.common.security.token.TokenCertificate;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

/**
 * @author wtk
 * @date 2022-08-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTokenCertificate extends TokenCertificate {

    Integer userId;

    String username;

    /**
     * 登录时间
     */
    Date loginTime;

    public UserTokenCertificate(Integer userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
