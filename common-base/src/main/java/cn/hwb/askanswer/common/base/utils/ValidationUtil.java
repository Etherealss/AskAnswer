package cn.hwb.askanswer.common.base.utils;

import javax.validation.ConstraintValidatorContext;

/**
 * @author hwb
 */
public class ValidationUtil {

    public static void customMessage(ConstraintValidatorContext ctx, String msg) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
