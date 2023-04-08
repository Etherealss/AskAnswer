package cn.hwb.askanswer.notification.infrastructure.pojo.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDTO {
    Long id;

    String type;

    Object props;

    Long rcvrId;

    Boolean isRead;

    Long creator;

    Date createTime;

    Date modifyTime;
}
