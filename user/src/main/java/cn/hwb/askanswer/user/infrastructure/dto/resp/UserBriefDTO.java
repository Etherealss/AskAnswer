package cn.hwb.askanswer.user.infrastructure.dto.resp;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class UserBriefDTO {
    Long id;

    String username;

    String avatar;

    Date birthday;

    String signature;

    Date createTime;
}
