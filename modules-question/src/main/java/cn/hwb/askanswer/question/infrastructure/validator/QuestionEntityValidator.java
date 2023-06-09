package cn.hwb.askanswer.question.infrastructure.validator;

import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.validation.entity.EntityValidator;
import cn.hwb.askanswer.question.infrastructure.pojo.entity.QuestionEntity;
import cn.hwb.askanswer.question.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hwb
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QuestionEntityValidator implements EntityValidator {
    private final QuestionService questionService;
    @Override
    public void validate(Long id) throws NotFoundException {
        Integer count = questionService.lambdaQuery()
                .eq(QuestionEntity::getId, id)
                .count();
        if (count == 0) {
            throw new NotFoundException(QuestionEntity.class, id.toString());
        }
    }
}
