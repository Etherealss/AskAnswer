package cn.hwb.common.security.auth.service;

import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.auth.exception.TokenException;
import cn.hwb.common.security.auth.interceptor.HeaderInterceptor;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author hwb
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserTokenAuthHandler implements IPreAuthHandler {

    @Override
    public boolean checkNeedAuth(Method method) {
        // 没有注解说明需要检查，返回true
        return method.getAnnotation(AnonymousAccess.class) == null;
    }

    /**
     * {@link HeaderInterceptor} 会获取Token对象并保存到 {@link UserSecurityContextHolder} 中
     * 此处从 {@link UserSecurityContextHolder#require()} 获取对象
     * @param method 用于获取注解
     */
    @Override
    public void doAuth(Method method) {
        try {
            UserSecurityContextHolder.require();
        } catch (TokenException e) {
            log.debug("请求Token校验不通过：{}", e.getMessage());
            throw e;
        }
    }
}
