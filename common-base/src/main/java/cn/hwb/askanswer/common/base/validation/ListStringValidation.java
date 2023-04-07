package cn.hwb.askanswer.common.base.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hwb
 
 * @see ListStringValidator 检查List里的String元素
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ListStringValidator.class})
public @interface ListStringValidation {
    String message() default "列表元素格式不符合要求";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String regexp() default "";

    int min() default 0;

    int max() default Integer.MAX_VALUE;
}
