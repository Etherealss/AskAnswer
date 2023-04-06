package cn.hwb.askanswer.notification.controller;

import cn.hwb.askanswer.common.base.enums.BaseEnum;
import cn.hwb.askanswer.common.base.enums.NotificationType;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.notification.infrastructure.pojo.resp.NotificationResp;
import cn.hwb.askanswer.notification.service.NotificationService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@ResponseAdvice
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/pages/notifications")
    public PageDTO<NotificationResp> page(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "unread", defaultValue = "false") Boolean unread) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        if (StringUtils.hasText(type)) {
            NotificationType notificationType = BaseEnum.fromName(NotificationType.class, type);
            return notificationService.page(userId, notificationType, cursor, size);
        } else {
            return notificationService.page(userId, null, cursor, size);
        }
    }
}
