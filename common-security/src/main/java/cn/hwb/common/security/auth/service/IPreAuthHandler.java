package cn.hwb.common.security.auth.service;

import cn.hwb.askanswer.common.base.exception.service.AuthenticationException;

import java.lang.reflect.Method;

/**
 * 可以通过重写该接口修改验证方案
 * @author wtk
 * @date 2022-10-07
 */
public interface IPreAuthHandler {

    /**
     * 判断是否需要进行校验
     * @param method 用于获取注解
     * @return
     */
    boolean checkNeedAuth(Method method);

    /**
     * 对一个Method对象进行注解检查
     * 如果 auth 不通过，抛出 {@code AuthenticationException} 异常以拦截请求
     * @param method 用于获取注解
     */
    void doAuth(Method method) throws AuthenticationException;
}
