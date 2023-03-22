package cn.hwb.askanswer.user.infrastructure.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$", message = "用户名只能包含这些字符“a-zA-Z0-9_-”，长度为3-16")
    String username;

    @NotBlank
    @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "密码应该加密，是一个具有64位的十六进制字符串")
    String password;

    @NotNull
    @Past
    Date birthday;
}
