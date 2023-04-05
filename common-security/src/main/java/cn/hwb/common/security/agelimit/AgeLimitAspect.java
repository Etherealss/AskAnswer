package cn.hwb.common.security.agelimit;

import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.utils.SpELParserUtils;
import cn.hwb.common.security.auth.exception.AgeLimitedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ParseException;

import java.lang.reflect.Method;

/**
 * 验证用户年龄段
 * @author wtk
 * @date 2023-03-23
 */
@Aspect
//@Component
@RequiredArgsConstructor
@Slf4j
@Deprecated
public class AgeLimitAspect {

    private final AgeLimitVerifier ageLimitVerifier;

    /**
     * 想要修改入参的值，必须使用AOP而不能使用SpringValidation，后者只有检查能力，没有修改能力
     * 但AOP可以获取方法参数并通过反射赋值
     * @param joinPoint
     * @param ageLimit
     * @throws AgeLimitedException 当前用户年龄段受限
     * @throws NotFoundException 目标不存在
     * @throws ParseException SpEL解析失败
     */
    @Before("@annotation(ageLimit)")
    public void escape(JoinPoint joinPoint, AgeLimit ageLimit) throws AgeLimitedException, NotFoundException {
        log.info("用户回答问题时进行年龄段验证");
        // 通过SpEL获取id
        String spel = ageLimit.value();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Long id = SpELParserUtils.parse(method, joinPoint.getArgs(), spel, Long.class);
        // 验证用户年龄与目标年龄段
        boolean verify = ageLimitVerifier.verify(id);
        if (!verify) {
            throw new AgeLimitedException("当前用户年龄段受限");
        }
    }
}
