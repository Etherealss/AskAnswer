package cn.hwb.askanswer.common.base.validation.entity;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wtk
 * @date 2023-03-24
 */
@Deprecated
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EntityExistValidator.class})
public @interface EntityValid {
    String message() default "Entity不存在";

    /**
     * 设置 EntityValidateHandler 的 BeanName
     * @return
     */
    String[] value() default {};

    Class<? extends EntityValidator>[] handlers() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}