package cn.hwb.askanswer.question.infrastructure.validator;

import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.validation.EntityValidator;
import cn.hwb.askanswer.question.infrastructure.pojo.entity.QuestionEntity;
import cn.hwb.askanswer.question.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wtk
 * @date 2023-03-24
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QuestionEntityValidator implements EntityValidator {
    private final QuestionService questionService;
    @Override
    public void validate(Long id) {
        Integer count = questionService.lambdaQuery()
                .eq(QuestionEntity::getId, id)
                .count();
        if (count == 0) {
            throw new NotFoundException(QuestionEntity.class, id.toString());
        }
    }
}
