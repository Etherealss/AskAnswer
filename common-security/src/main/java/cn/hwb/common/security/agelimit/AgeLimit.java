package cn.hwb.common.security.agelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AgeLimit {
    /**
     * 用于获取要验证年龄段的问题，使用支持SpEL
     * @return
     */
    String value();
}
