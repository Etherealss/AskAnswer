package cn.hwb.askanswer.common.base.utils;

import javax.validation.ConstraintValidatorContext;

/**
 * @author wtk
 * @date 2023-03-24
 */
public class ValidationUtil {

    public static void customMessage(ConstraintValidatorContext ctx, String msg) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
