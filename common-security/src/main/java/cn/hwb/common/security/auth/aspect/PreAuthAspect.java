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
 * @author hwb
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PreAuthAspect {

    private final List<IPreAuthHandler> preAuthHandlers;
    /**
     * AOP签名
     */
    @Pointcut("execution(* cn.hwb..controller..*.*(..))")
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
