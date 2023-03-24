package cn.hwb.common.security.xss;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * xss 转义
 * @see XssEscapeAspect 声明在方法上以开启方法参数、参数类、类字段上以开启检查
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface XssEscape {
}
