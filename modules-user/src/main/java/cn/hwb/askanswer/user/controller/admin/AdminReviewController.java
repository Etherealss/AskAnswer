package cn.hwb.askanswer.user.controller.admin;

import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserAuthDTO;
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
@RequestMapping("/admins/review")
@RequiredArgsConstructor
@ResponseAdvice
public class AdminReviewController {

    private final UserService userService;

    @RequiredRoles(RoleConstant.ADMIN)
    @PutMapping("/users/{userId}")
    public void reviewPass(@PathVariable Long userId) {
        userService.reviewPass(userId);
    }

    @RequiredRoles(RoleConstant.ADMIN)
    @DeleteMapping("/users/{userId}")
    public void reviewFail(@PathVariable Long userId) {
        userService.reviewFail(userId);
    }

    @RequiredRoles(RoleConstant.ADMIN)
    @GetMapping("/pages/users")
    public PageDTO<UserAuthDTO> pageReviewImg(
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "current", defaultValue = "1") int currentPage) {
       return userService.page4Review(currentPage, size);
    }

    @RequiredRoles(RoleConstant.ADMIN)
    @GetMapping("/users/{userId}")
    public UserAuthDTO getReviewImg(@PathVariable Long userId) {
        return userService.get4Review(userId);
    }
}
