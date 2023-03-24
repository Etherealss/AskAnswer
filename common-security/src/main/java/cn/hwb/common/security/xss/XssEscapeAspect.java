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
 * @author wtk
 * @date 2023-03-23
 */
@Aspect
@Component
@ConditionalOnProperty(value = "app.security.xss.enabled", havingValue = "true")
@Slf4j
public class XssEscapeAspect {

    @Before("@annotation(cn.hwb.common.security.xss.XssEscape)")
    public void escape(JoinPoint joinPoint) throws Exception {
        log.info("进行XSS转义检查");
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null && arg.getClass().isAnnotationPresent(XssEscape.class)) {
                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(XssEscape.class) && field.getType().equals(String.class)) {
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
}
