package cn.hwb.askanswer.answer.service.answer;

import cn.hwb.askanswer.answer.infrastructure.converter.AnswerConverter;
import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateAnswerRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerAcceptRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerRequest;
import cn.hwb.askanswer.answer.mapper.AnswerMapper;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.pojo.event.question.QuestionCreatorValidateEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService extends ServiceImpl<AnswerMapper, AnswerEntity> {
    public static final int PAGE_SIZE = 10;

    private final AnswerConverter converter;
    private final AnswerMapper questionMapper;
    private final ApplicationEventPublisher eventPublisher;

    public Long publish(Long questionId, CreateAnswerRequest req) {
        AnswerEntity answerEntity = converter.toEntity(req);
        answerEntity.setQuestionId(questionId);
        this.save(answerEntity);
        return answerEntity.getId();
    }

    public void update(Long questionId, Long answerId, Long answerCreator, UpdateAnswerRequest req) {
        preCheck(questionId, answerId, answerCreator);
        AnswerEntity updateEntity = converter.toEntity(req);
        boolean update = this.lambdaUpdate()
                .eq(AnswerEntity::getId, answerId)
                .eq(AnswerEntity::getQuestionId, questionId)
                .update(updateEntity);
    }

    public void update(Long questionId, Long answerId, Long questionCreator,
                       UpdateAnswerAcceptRequest req) {
        eventPublisher.publishEvent(
                new QuestionCreatorValidateEvent(questionId, questionCreator)
        );
        preCheck(null, answerId, null);
        this.lambdaUpdate()
                .eq(AnswerEntity::getId, answerId)
                .eq(AnswerEntity::getQuestionId, questionId)
                .set(AnswerEntity::getIsAccepted, req.getIsAccepted())
                .update();
    }

    public void deleteById(Long answerId, Long userId) {
        preCheck(null, answerId, userId);
        this.lambdaUpdate()
                .eq(AnswerEntity::getId, answerId)
                .eq(AnswerEntity::getCreator, userId)
                .remove();
    }

    public AnswerDTO findById(Long questionId) {
        AnswerEntity entity = this.lambdaQuery()
                .eq(AnswerEntity::getId, questionId)
                .one();
        if (entity == null) {
            throw new NotFoundException(AnswerEntity.class, questionId.toString());
        }
        return converter.toDto(entity);
    }

    private void preCheck(Long questionId, Long answerId, Long answerCreator) {
        AnswerEntity entity = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .select(AnswerEntity::getQuestionId, AnswerEntity::getCreator)
                .one();
        if (entity == null) {
            throw new NotFoundException(AnswerEntity.class, answerId.toString());
        }
        if (answerCreator != null && !answerCreator.equals(entity.getCreator())) {
            throw new NotCreatorException(answerCreator, answerId);
        }
        if (questionId != null && !questionId.equals(entity.getQuestionId())) {
            String format = String.format("'%s'回答的问题并非'%s'",
                    answerId.toString(), questionId.toString());
            throw new BadRequestException(ResultCode.ANSWER_TARGET_NOT_MATCH, format);
        }
    }

    public PageDTO<AnswerDTO> page(Long cursorId, int size, Long questionId) {
        List<AnswerDTO> records = this.lambdaQuery()
                .eq(AnswerEntity::getQuestionId, questionId)
                .gt(AnswerEntity::getId, cursorId)
                .orderByDesc(AnswerEntity::getId)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return PageDTO.<AnswerDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }
}
