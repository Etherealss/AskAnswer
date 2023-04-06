package cn.hwb.common.security.token.user;

import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import cn.hwb.common.security.token.TokenCertificate;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

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

    Long userId;

    String username;

    /**
     * 生日
     */
    Date birthday;

    /**
     * 年龄段
     */
    AgeBracketEnum ageBracket;

    /**
     * 身份
     */
    List<String> roles;

}
