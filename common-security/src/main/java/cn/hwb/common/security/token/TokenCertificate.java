package cn.hwb.common.security.token;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author hwb
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class TokenCertificate {

    /**
     * 不重复的字符串，UUID生成
     */
    String token;

    /**
     * token 过期的日期
     */
    Date expiryDate;
}
