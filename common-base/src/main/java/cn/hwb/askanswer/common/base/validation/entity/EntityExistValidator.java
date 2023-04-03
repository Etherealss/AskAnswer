package cn.hwb.askanswer.common.base.validation.entity;

import cn.hutool.extra.spring.SpringUtil;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.utils.ValidationUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wtk
 * @date 2023-03-24
 */
@Deprecated
public class EntityExistValidator implements ConstraintValidator<EntityValid, Long> {
    private Set<EntityValidator> validators;

    @Override
    public void initialize(EntityValid constraintAnnotation) {
        this.validators = new HashSet<>();
        for (Class<? extends EntityValidator> validatorClass : constraintAnnotation.handlers()) {
            this.validators.add(SpringUtil.getBean(validatorClass));
        }
        for (String beanName : constraintAnnotation.value()) {
            this.validators.add(SpringUtil.getBean(beanName));
        }
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        for (EntityValidator validator : validators) {
            try {
                validator.validate(id);
            } catch (NotFoundException e) {
                ValidationUtil.customMessage(context, e.getIdentification() + "对应的Entity不存在");
                return false;
            }
        }
        return true;
    }
}