package cn.hwb.common.security.xss;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * xss 转义
 * 用法：声明在方法上、方法参数的类上、类中需要检查的字段上，以开启检查
 * @see XssEscapeAspect
 * @author hwb
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface XssEscape {
}
