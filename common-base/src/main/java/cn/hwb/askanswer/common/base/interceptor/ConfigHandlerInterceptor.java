package cn.hwb.askanswer.common.base.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author wtk
 * @date 2022-08-30
 */
public interface ConfigHandlerInterceptor extends HandlerInterceptor {

    default String[] getPathPatterns() {
        return new String[]{"/**"};
    }

    default String[] getExcludePathPatterns() {
        return new String[0];
    }

    default int getOrder() {
        return 0;
    }
}
