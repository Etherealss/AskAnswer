package cn.hwb.common.security.auth.service;

import cn.hwb.common.base.exception.service.AuthenticationException;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
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
public class UserPreAuthHandler implements IPreAuthHandler {

    @Override
    public boolean checkNeedAuth(Method method) {
        if (method.getAnnotation(AnonymousAccess.class) != null) {
            log.info("匿名访问接口，无需检验");
            return false;
        }
        return true;
    }

    @Override
    public void doAuth(Method method) {
        try {
            // TODO 年龄段验证
        } catch (AuthenticationException e) {
            log.debug("请求认证不通过：{}", e.getMessage());
            throw e;
        }
    }
}
