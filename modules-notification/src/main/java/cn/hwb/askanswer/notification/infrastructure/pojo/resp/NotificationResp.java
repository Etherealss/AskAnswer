package cn.hwb.askanswer.notification.infrastructure.pojo.resp;

import cn.hwb.askanswer.common.base.enums.NotificationType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResp {
    Long id;

    NotificationType type;

    Object props;

    Long rcvrId;

    Boolean isRead;

    Long creator;

    Date createTime;

    Date modifyTime;
}