package cn.hwb.common.security.auth.annotation;


import cn.hwb.common.security.auth.enums.Logical;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色认证：必须具有指定角色标识才能进入该方法
 * 
 * @author hwb
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiredRoles {
    /**
     * 需要校验的角色标识
     */
    String[] value() default {};

    /**
     * 验证逻辑：AND是所列的角色都要有；OR是只需要满足其中一个
     */
    Logical logical() default Logical.AND;
}
