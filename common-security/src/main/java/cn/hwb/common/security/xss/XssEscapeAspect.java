package cn.hwb.common.security.xss;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 用法见 {@link XssEscape}
 * @author hwb
 */
@Aspect
@Component
@ConditionalOnProperty(value = "app.security.xss.enabled", havingValue = "true")
@Slf4j
public class XssEscapeAspect {

    /**
     * 想要修改入参的值，必须使用AOP而不能使用SpringValidation，后者只有检查能力，没有修改能力
     * 但AOP可以获取方法参数并通过反射赋值
     * @param joinPoint
     * @throws Exception
     */
    @Before("@annotation(cn.hwb.common.security.xss.XssEscape)")
    public void escape(JoinPoint joinPoint) throws Exception {
        log.info("进行XSS转义检查");
        // 遍历方法参数，查看每一个方法参数的类型
        for (Object arg : joinPoint.getArgs()) {
            if (arg == null) {
                continue;
            }
            // 如果方法参数类型上有 @XssEscape 注解则继续，否则进行下一次循环
            if (!arg.getClass().isAnnotationPresent(XssEscape.class)) {
                continue;
            }
            // 获取该参数类型里的字段
            Field[] fields = arg.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 找到类字段上有 @XssEscape 注解的字段
                if (field.isAnnotationPresent(XssEscape.class) &&
                        field.getType().equals(String.class)) {
                    // 进行Xss防注入
                    field.setAccessible(true);
                    String value = (String) field.get(arg);
                    if (value != null) {
                        String escapedValue = StringEscapeUtils.escapeHtml4(value);
                        escapedValue = StringEscapeUtils.escapeEcmaScript(escapedValue);
                        field.set(arg, escapedValue);
                    }
                }
            }
        }
    }
}
