package cn.hwb.askanswer.common.base.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 允许List为空或字符串为空，但只要提供了字符串，就需要满足指定格式
 * @author wtk
 * @date 2022-08-14
 */
public class ListStringValidator implements ConstraintValidator<ListStringValidation, List<String>> {
    private String regexp;
    private int min;
    private int max;

    @Override
    public void initialize(ListStringValidation constraint) {
        this.regexp = constraint.regexp();
        this.min = constraint.min();
        this.max = constraint.max();
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (String element : value) {
            if (element == null) {
                return true;
            }
            if (element.length() < min || element.length() > max) {
                return false;
            }
        }
        if (StringUtils.hasText(regexp)) {
            Pattern pattern = Pattern.compile(regexp);
            for (String element : value) {
                if (!pattern.matcher(element).matches()) {
                    return false;
                }
            }
        }
        return true;
    }
}
