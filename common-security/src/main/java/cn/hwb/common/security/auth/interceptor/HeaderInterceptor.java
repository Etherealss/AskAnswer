package cn.hwb.common.security.auth.interceptor;

import cn.hwb.askanswer.common.base.interceptor.ConfigHandlerInterceptor;
import cn.hwb.common.security.config.UserCertificateConfig;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import cn.hwb.common.security.token.user.UserTokenCertificate;
import cn.hwb.common.security.token.user.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取 Token 并将相关信息存入 SecurityContextHolder 中
 * @author hwb
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HeaderInterceptor implements ConfigHandlerInterceptor {

    private final UserCertificateConfig userCertificateConfig;
    private final UserTokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        setUserToken(request);
        return true;
    }

    private void setUserToken(HttpServletRequest request) {
        String userToken = request.getHeader(userCertificateConfig.getHeaderName());
        if (StringUtils.hasText(userToken)) {
            UserTokenCertificate tokenCertificate = tokenService.getToken(userToken);
            if (tokenCertificate != null) {
                UserSecurityContextHolder.set(tokenCertificate);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        UserSecurityContextHolder.remove();
    }

    @Override
    public int getOrder() {
        return -10;
    }
}
