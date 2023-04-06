package cn.hwb.askanswer.notification.controller;

import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.notification.infrastructure.pojo.resp.NotificationResp;
import cn.hwb.askanswer.notification.service.NotificationService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "current", defaultValue = "1") int currentPage,
            @RequestParam(value = "unread", defaultValue = "false") Boolean unread) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        return notificationService.page(userId, currentPage, size, unread);
    }
}
