package cn.hwb.askanswer.answer.infrastructure.validator;

import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import cn.hwb.askanswer.answer.service.answer.AnswerService;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wtk
 * @date 2023-03-25
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AnswerEntityValidator implements EntityValidator {
    private final AnswerService answerService;
    @Override
    public void validate(Long id) {
        Integer count = answerService.lambdaQuery()
                .eq(AnswerEntity::getId, id)
                .count();
        if (count == 0) {
            throw new NotFoundException(AnswerEntity.class, id.toString());
        }
    }
}
