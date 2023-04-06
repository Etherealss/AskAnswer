package cn.hwb.askanswer.notification.infrastructure.pojo.request;

import cn.hwb.askanswer.common.base.enums.NotificationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PublishNotificationCommand {
    @NotNull
    NotificationType type;

    @NotNull
    Object props;

    @NotNull
    Long rcvrId;
}
