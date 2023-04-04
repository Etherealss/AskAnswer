package cn.hwb.askanswer.user.infrastructure.pojo.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAuthDTO {
    Long id;

    String username;

    Date birthday;

    Date createTime;

    String reviewImg;
}
