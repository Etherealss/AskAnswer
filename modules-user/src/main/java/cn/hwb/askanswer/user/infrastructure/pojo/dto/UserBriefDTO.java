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
public class UserBriefDTO {
    Long id;

    String username;

    String avatar;

    Date birthday;

    String signature;

    Date createTime;

    Integer ageBracket;

    List<String> roles;
}
