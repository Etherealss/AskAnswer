package cn.hwb.askanswer.notification.service;

import cn.hwb.askanswer.common.base.pojo.event.PublishNotificationEvent;
import cn.hwb.askanswer.notification.infrastructure.converter.NotificationConverter;
import cn.hwb.askanswer.notification.infrastructure.pojo.request.PublishNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationListener {
    private final NotificationConverter converter;
    private final NotificationService notificationService;

    @EventListener(PublishNotificationEvent.class)
    public void handlerEvent(PublishNotificationEvent event) {
        PublishNotificationRequest command = converter.toCommand(event);
        notificationService.publish(command);
    }
}
