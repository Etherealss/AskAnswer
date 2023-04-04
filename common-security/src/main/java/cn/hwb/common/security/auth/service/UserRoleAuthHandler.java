package cn.hwb.common.security.auth.service;

import cn.hwb.askanswer.common.base.exception.service.AuthenticationException;
import cn.hwb.common.security.auth.annotation.RequiredRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author wtk
 * @date 2022-10-07
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRoleAuthHandler implements IPreAuthHandler {

    private final LogicAuthService logicAuthService;

    @Override
    public boolean checkNeedAuth(Method method) {
        // 不为null时说明要进行校验，返回true
        return method.getAnnotation(RequiredRoles.class) != null;
    }

    @Override
    public void doAuth(Method method) {
        RequiredRoles requiresRoles = method.getAnnotation(RequiredRoles.class);
        assert requiresRoles != null;
        try {
            // 校验 @RequiresRoles 注解
            logicAuthService.checkRole(requiresRoles);
        } catch (AuthenticationException e) {
            log.debug("请求身份认证不通过：{}", e.getMessage());
            throw e;
        }
    }
}
