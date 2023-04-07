package cn.hwb.askanswer.user.controller.admin;

import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserAuthDTO;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.common.security.auth.annotation.RequiredRoles;
import cn.hwb.common.security.auth.enums.AuthConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping("/admins/review")
@RequiredArgsConstructor
@ResponseAdvice
public class AdminReviewController {

    private final UserService userService;

    @RequiredRoles(AuthConstants.ADMIN)
    @PutMapping("/users/{userId}")
    public void reviewPass(@PathVariable Long userId) {
        userService.reviewPass(userId);
    }

    @RequiredRoles(AuthConstants.ADMIN)
    @DeleteMapping("/users/{userId}")
    public void reviewFail(@PathVariable Long userId) {
        userService.reviewFail(userId);
    }

    @RequiredRoles(AuthConstants.ADMIN)
    @GetMapping("/pages/users")
    public PageDTO<UserAuthDTO> pageReviewImg(
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "current", defaultValue = "1") int currentPage) {
       return userService.page4Review(currentPage, size);
    }

    @RequiredRoles(AuthConstants.ADMIN)
    @GetMapping("/users/{userId}")
    public UserAuthDTO getReviewImg(@PathVariable Long userId) {
        return userService.get4Review(userId);
    }
}
