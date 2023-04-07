package cn.hwb.askanswer.user.infrastructure.pojo.request;

import cn.hwb.common.security.xss.XssEscape;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserInfoRequest {
    @Size(min = 0, max = 100)
    @XssEscape
    String signature;

    @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "密码应该加密，是一个具有64位的十六进制字符串")
    String curPassword;

    @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "密码应该加密，是一个具有64位的十六进制字符串")
    String newPassword;

    @Past
    Date birthday;
}
