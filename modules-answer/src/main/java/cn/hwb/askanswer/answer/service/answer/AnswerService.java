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
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.common.security.agelimit.AgeLimitVerifier;
import cn.hwb.common.security.auth.exception.AgeLimitedException;
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
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final AgeLimitVerifier ageLimitVerifier;

    public Long publish(Long questionId, CreateAnswerRequest req) {
        boolean verify = ageLimitVerifier.verify(questionId);
        if (!verify) {
            throw new AgeLimitedException("当前用户年龄段受限");
        }
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
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, questionId.toString()));
        this.anonymousHandle(entity);
        AnswerDTO answerDTO = converter.toDto(entity);
        answerDTO.setCreator(userService.getBriefById(entity.getCreator()));
        return answerDTO;
    }

    private void preCheck(Long questionId, Long answerId, Long answerCreator) {
        AnswerEntity entity = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .select(AnswerEntity::getQuestionId, AnswerEntity::getCreator)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, questionId.toString()));
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
                .map(this::anonymousHandle)
                .map(e -> {
                    UserBriefDTO userBrief = userService.getBriefById(e.getCreator());
                    AnswerDTO answerDTO = converter.toDto(e);
                    answerDTO.setCreator(userBrief);
                    return answerDTO;
                }).collect(Collectors.toList());
        return PageDTO.<AnswerDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }

    private AnswerEntity anonymousHandle(AnswerEntity entity) {
        if (entity.getIsAnonymous()) {
            entity.setCreator(null);
        }
        return entity;
    }
}
