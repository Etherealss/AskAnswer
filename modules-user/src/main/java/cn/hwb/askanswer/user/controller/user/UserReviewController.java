package cn.hwb.askanswer.user.controller.user;

import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping("/users/review")
@RequiredArgsConstructor
@ResponseAdvice
public class UserReviewController {
    private final UserService userService;

    @GetMapping
    public String getReviewImg() {
        Long userId = UserSecurityContextHolder.require().getUserId();
        return userService.getReviewImg(userId);
    }
}
