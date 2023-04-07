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

    String token;

    /**
     * token 过期时间
     */
    Date expiryDate;
}
