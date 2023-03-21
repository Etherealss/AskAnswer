package cn.hwb.common.security.token.user;

import cn.hwb.common.security.token.TokenCertificate;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author wtk
 * @date 2022-08-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTokenCertificate extends TokenCertificate {

    Integer userId;

    String username;

    /**
     * 生日
     */
    Date birthday;

    public UserTokenCertificate(Integer userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
