package cn.hwb.askanswer.notification.infrastructure.pojo.request;

import cn.hwb.askanswer.common.base.pojo.vo.NotificationTemplate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PublishNotificationRequest {
    @NotNull
    String type;

    @NotNull
    NotificationTemplate props;

    @NotNull
    Long rcvrId;
}
