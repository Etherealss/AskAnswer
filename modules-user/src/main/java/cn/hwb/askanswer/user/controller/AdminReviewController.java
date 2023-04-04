package cn.hwb.askanswer.user.controller;

import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.common.security.auth.annotation.RequiredRoles;
import cn.hwb.common.security.enums.RoleConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author wtk
 * @date 2023-04-04
 */
@Slf4j
@RestController
@RequestMapping("/admins/review/users/{userId}")
@RequiredArgsConstructor
@ResponseAdvice
public class AdminReviewController {

    private final UserService userService;

    @RequiredRoles(RoleConstant.ADMIN)
    @PutMapping
    public void reviewPass(@PathVariable Long userId) {
        userService.reviewPass(userId);
    }

    @RequiredRoles(RoleConstant.ADMIN)
    @DeleteMapping
    public void reviewFail(@PathVariable Long userId) {
        userService.reviewFail(userId);
    }

    @RequiredRoles(RoleConstant.ADMIN)
    @GetMapping
    public String getReviewImg(@PathVariable Long userId) {
       return userService.getReviewImg(userId);
    }
}
