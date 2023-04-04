package cn.hwb.askanswer.user.controller;

import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.askanswer.user.service.user.avatar.UserAvatarService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wang tengkun
 * @date 2023/4/4
 */
@Slf4j
@RestController
@RequestMapping("/users/avatars")
@RequiredArgsConstructor
@ResponseAdvice
public class UserAvatarController {

    private final UserAvatarService avatarService;
    private final UserService userService;

    @GetMapping("/default")
    public String getAvatar() {
        return avatarService.getDefaultAvatar();
    }

    @PutMapping
    public String uploadAvatar(@RequestParam("avatarFile") MultipartFile avatarFile) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        return userService.updateAvatar(userId, avatarFile);
    }
}
