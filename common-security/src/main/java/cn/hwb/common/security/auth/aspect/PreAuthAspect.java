package cn.hwb.common.security.auth.aspect;


import cn.hwb.common.security.auth.service.IPreAuthHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author wtk
 * @date 2022-08-30
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PreAuthAspect {

    /**
     * 定义AOP签名 (切入所有使用鉴权注解的方法)
     */
    public static final String POINTCUR_REQUEST_MAPPING = ""
            + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.RequestMapping)";

    public static final String POINTCUR_CONTROLLER = "execution(* com.nowander..controller..*.*(..))";

    private final List<IPreAuthHandler> preAuthHandlers;
    /**
     * AOP签名
     */
    @Pointcut(POINTCUR_CONTROLLER)
    public void pointcut() {
    }

    /**
     * @param joinPoint 切面对象
     * @return 底层方法执行后的返回值
     * @throws Throwable 底层方法抛出的异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 注解鉴权
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        for (IPreAuthHandler preAuthHandler : preAuthHandlers) {
            if (preAuthHandler.checkNeedAuth(method)) {
                preAuthHandler.doAuth(method);
            }
        }
        // 执行原有逻辑
        return joinPoint.proceed();
    }
}
