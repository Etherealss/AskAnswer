package cn.hwb.askanswer.common.base.validation.entity;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author wtk
 * @date 2023-03-24
 */
@Aspect
@Component
@Slf4j
public class EntityExistenceAspect {

    @Around("@annotation(a)")
    public Object validateEntityExistence(ProceedingJoinPoint joinPoint, EntityExist a) throws Throwable {
        log.debug("进行实体存在性校验");
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget()
                .getClass()
                .getMethod(
                        joinPoint.getSignature().getName(),
                        methodSignature.getParameterTypes()
                );
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            if (!arg.getClass().isAssignableFrom(Long.class)) {
                continue;
            }
            Long id = (Long) arg;
            Parameter parameter = method.getParameters()[i];
            EntityExist annotation = parameter.getAnnotation(EntityExist.class);
            if (annotation != null) {
                for (String beanName : annotation.value()) {
                    EntityValidator validator = SpringUtil.getBean(beanName);
                    validator.validate(id);
                }
            }
        }
        return joinPoint.proceed(args);
    }
}