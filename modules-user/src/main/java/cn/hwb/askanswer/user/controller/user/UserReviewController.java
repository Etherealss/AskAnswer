package cn.hwb.askanswer.user.controller.user;

import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wtk
 * @date 2023-04-04
 */
@Slf4j
@RestController
@RequestMapping("/users/review")
@RequiredArgsConstructor
@ResponseAdvice
public class UserReviewController {
    private final UserService userService;

    @PutMapping
    public void uploadReviewImg(@RequestParam("imgFile") MultipartFile imgFile) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        userService.updateReviewImg(userId, imgFile);
    }

    @GetMapping
    public String getReviewImg() {
        Long userId = UserSecurityContextHolder.require().getUserId();
        return userService.getReviewImg(userId);
    }
}
