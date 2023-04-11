package cn.hwb.askanswer.user.controller.user;

import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.infrastructure.pojo.request.CreateUserRequest;
import cn.hwb.askanswer.user.infrastructure.pojo.request.UpdateUserInfoRequest;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.Date;
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
    public Long register(@RequestParam("username")
                         @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$", message = "用户名只能包含这些字符“a-zA-Z0-9_-”，长度为3-16")
                         String username,
                         @RequestParam("password")
                         @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "密码应该加密，是一个具有64位的十六进制字符串")
                         String password,
                         @RequestParam("birthday")
                         @Past
                         Long birthday,
                         @RequestParam("imgFile") MultipartFile imgFile) {
        CreateUserRequest request = new CreateUserRequest()
                .setUsername(username)
                .setPassword(password)
                .setBirthday(new Date(birthday))
                .setReviewImg(imgFile);
        return userService.createUser(request);
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

    @GetMapping("/users")
    public UserBriefDTO findCurUser() {
        Long userId = UserSecurityContextHolder.require().getUserId();
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
    public void updateById(@RequestBody @Validated UpdateUserInfoRequest req) {
        userService.update(UserSecurityContextHolder.require().getUserId(), req);
    }
}
