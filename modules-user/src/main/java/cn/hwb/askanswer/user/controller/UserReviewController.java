package cn.hwb.askanswer.user.controller;

import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.askanswer.user.service.user.review.UserReviewService;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
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
    private final UserReviewService userReviewService;
    private final UserService userService;
    @PostMapping
    @AnonymousAccess
    public void uploadReviewImg(@RequestParam("imgFile") MultipartFile imgFile) {
        userReviewService.uploadAvatar(imgFile, UserSecurityContextHolder.require().getUserId());
    }
}
