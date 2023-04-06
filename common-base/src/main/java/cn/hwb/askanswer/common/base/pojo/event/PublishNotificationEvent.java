package cn.hwb.askanswer.common.base.pojo.event;

import cn.hwb.askanswer.common.base.enums.NotificationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 发布通知的事件
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

    public PublishNotificationEvent(Long userId, NotificationType type, Object props, Long rcvrId) {
        super(userId);
        this.type = type;
        this.props = props;
        this.rcvrId = rcvrId;
    }
}