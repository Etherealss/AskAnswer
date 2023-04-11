package cn.hwb.askanswer.user.infrastructure.pojo.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    String username;
    String password;
    Date birthday;
    MultipartFile reviewImg;
}
