package cn.hwb.askanswer.common.base.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 自动配置的拦截器
 * @author hwb
 */
public interface ConfigHandlerInterceptor extends HandlerInterceptor {

    /**
     * 要拦截的路径，默认是所有路径
     * @return
     */
    default String[] getPathPatterns() {
        return new String[]{"/**"};
    }

    /**
     * 不拦截的路径，默认为"无"
     * @return
     */
    default String[] getExcludePathPatterns() {
        return new String[0];
    }

    /**
     * 拦截器的拦截顺序，默认是0，与其他拦截器一样
     * @return
     */
    default int getOrder() {
        return 0;
    }
}
