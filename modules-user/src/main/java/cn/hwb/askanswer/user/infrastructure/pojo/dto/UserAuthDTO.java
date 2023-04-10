package cn.hwb.askanswer.user.infrastructure.pojo.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAuthDTO {
    Long id;

    String username;

    Date birthday;

    Date createTime;

    String reviewImg;

    List<String> roles;
}
