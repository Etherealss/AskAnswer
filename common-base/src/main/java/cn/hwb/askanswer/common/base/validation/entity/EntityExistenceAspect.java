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
 * @author hwb
 */
@Aspect
@Component
@Slf4j
public class EntityExistenceAspect {

    /**
     * 拦截待了 @EntityExist 注解的方法
     * @param joinPoint
     * @param a
     * @return
     * @throws Throwable
     */
    @Around("@annotation(a)")
    public Object validateEntityExistence(ProceedingJoinPoint joinPoint, EntityExist a) throws Throwable {
        log.debug("AOP 校验实体是否存在");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget()
                .getClass()
                .getMethod(
                        joinPoint.getSignature().getName(),
                        methodSignature.getParameterTypes()
                );
        // 遍历方法参数，找到带 @EntityExist 注解的参数，这个参数就是要检查的ID
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            // ID只能是Long类型，因此如果不是则跳过
            if (!arg.getClass().isAssignableFrom(Long.class)) {
                continue;
            }
            Long id = (Long) arg;
            // 找到当前参数对应的参数注解，获取其中的 value 值，用 value 来获取检查的 EntityValidator 对象进行检查
            Parameter parameter = method.getParameters()[i];
            EntityExist annotation = parameter.getAnnotation(EntityExist.class);
            if (annotation != null) {
                for (String beanName : annotation.value()) {
                    EntityValidator validator = SpringUtil.getBean(beanName);
                    // validate可能会抛出 NotFoundException，说明ID对应的实体不存在
                    validator.validate(id);
                }
            }
        }
        // 检查完毕，继续执行方法
        return joinPoint.proceed(args);
    }
}