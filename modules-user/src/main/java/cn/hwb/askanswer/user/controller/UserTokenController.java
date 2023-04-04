package cn.hwb.askanswer.user.controller;

import cn.hwb.askanswer.user.infrastructure.pojo.request.UserLoginRequest;
import cn.hwb.askanswer.user.service.user.UserAuthService;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserTokenCertificate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Slf4j
@RestController
@RequestMapping("/auth/users/token")
@RequiredArgsConstructor
@ResponseAdvice
public class UserTokenController {

    private final UserAuthService userAuthService;

    @PostMapping
    @AnonymousAccess
    public UserTokenCertificate login(@RequestBody @Validated UserLoginRequest req) {
        return userAuthService.login(req);
    }

    @GetMapping("/{token}")
    @AnonymousAccess
    public Boolean verifyToken(@PathVariable String token) {
        return userAuthService.verifyToken(token);
    }

    @DeleteMapping("/{token}")
    public void logout(@PathVariable String token) {
        userAuthService.invalidateToken(token);
    }
}
