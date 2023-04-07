package cn.hwb.askanswer.common.base.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加该注解可以跳过 CurrentUserArgumentResolver
 * @author hwb
 */
@Target({ElementType.PARAMETER})  //表示贴在参数上
@Retention(RetentionPolicy.RUNTIME)
public @interface NotLoginUserParam {
}
