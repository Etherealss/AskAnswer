package cn.hwb.askanswer.user.controller.user;

import cn.hwb.askanswer.user.infrastructure.pojo.request.CreateUserRequest;
import cn.hwb.askanswer.user.infrastructure.pojo.request.UpdateUserSimpleInfoRequest;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@ResponseAdvice
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    @AnonymousAccess
    public Long register(@RequestBody @Validated CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    @GetMapping("/users/usernames/{username}")
    @AnonymousAccess
    public Boolean usernameExist(@PathVariable @Pattern(
            regexp = "^[a-zA-Z0-9_-]{3,16}$",
            message = "用户名只能包含这些字符“a-zA-Z0-9_-”，长度为3-16"
    ) String username) {
        log.trace("查看用户名是否已存在：{}", username);
        return userService.checkUsernameExists(username);
    }

    @GetMapping("/users/ids/{userId}")
    @AnonymousAccess
    public UserBriefDTO findById(@PathVariable Long userId) {
        return userService.getBriefById(userId);
    }

    @GetMapping("/list/users/ids")
    @AnonymousAccess
    public List<UserBriefDTO> findBatchById(@RequestParam(value = "ids", defaultValue = "") List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return userService.getBatchBriefsByIds(ids);
    }

    @PutMapping("/users")
    public void updateById(@RequestBody @Validated UpdateUserSimpleInfoRequest req) {
        userService.update(UserSecurityContextHolder.require().getUserId(), req);
    }
}
