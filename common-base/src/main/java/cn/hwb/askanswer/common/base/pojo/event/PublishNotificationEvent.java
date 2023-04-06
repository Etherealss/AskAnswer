package cn.hwb.askanswer.common.base.pojo.event;

import cn.hwb.askanswer.common.base.enums.NotificationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PublishNotificationEvent extends DomainEvent {
    NotificationType type;
    Object props;
    Long rcvrId;

    public PublishNotificationEvent(Long userId) {
        super(userId);
    }
}