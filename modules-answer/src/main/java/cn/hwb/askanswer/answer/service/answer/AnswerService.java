package cn.hwb.askanswer.answer.service.answer;

import cn.hwb.askanswer.answer.infrastructure.converter.AnswerConverter;
import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateAnswerRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerAcceptRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerRequest;
import cn.hwb.askanswer.answer.mapper.AnswerMapper;
import cn.hwb.askanswer.common.base.exception.rest.ParamErrorException;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.pojo.event.question.QuestionCreatorValidateEvent;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
        LambdaQueryChainWrapper<AnswerEntity> lambdaQuery = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId);
        if (answerCreator != null) {
            lambdaQuery = lambdaQuery.select(AnswerEntity::getCreator);
        }
        if (questionId != null) {
            lambdaQuery = lambdaQuery.select(AnswerEntity::getQuestionId);
        }
        AnswerEntity entity = lambdaQuery.one();
        if (entity == null) {
            throw new NotFoundException(AnswerEntity.class, answerId.toString());
        }
        if (answerCreator != null && !entity.getCreator().equals(answerCreator)) {
            throw new NotCreatorException(answerCreator, answerId);
        }
        if (questionId != null && !entity.getQuestionId().equals(questionId)) {
            throw new ParamErrorException("回答对应的问题ID不匹配");
        }
    }

    public PageDTO<AnswerDTO> page(Long cursorId, int size) {
        List<AnswerDTO> records = this.lambdaQuery()
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
