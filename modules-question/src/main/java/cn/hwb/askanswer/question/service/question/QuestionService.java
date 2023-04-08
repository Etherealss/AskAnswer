package cn.hwb.askanswer.question.service.question;

import cn.hwb.askanswer.collection.service.CollectionRelationService;
import cn.hwb.askanswer.collection.service.CollectionService;
import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import cn.hwb.askanswer.common.base.enums.NotificationType;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.notification.service.NotificationService;
import cn.hwb.askanswer.question.infrastructure.converter.QuestionConverter;
import cn.hwb.askanswer.question.infrastructure.pojo.dto.QuestionDTO;
import cn.hwb.askanswer.question.infrastructure.pojo.entity.QuestionEntity;
import cn.hwb.askanswer.question.infrastructure.pojo.request.CreateQuestionRequest;
import cn.hwb.askanswer.question.infrastructure.pojo.request.UpdateQuestionRequest;
import cn.hwb.askanswer.question.mapper.QuestionMapper;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.service.user.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hwb
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService extends ServiceImpl<QuestionMapper, QuestionEntity> {
    public static final int PAGE_SIZE = 10;

    private final QuestionConverter converter;
    private final QuestionMapper questionMapper;
    private final UserService userService;
    private final NotificationService notificationService;
    private final CollectionRelationService collectionRelationService;
    private final CollectionService collectionService;

    public Long publish(CreateQuestionRequest req, AgeBracketEnum ageBracket) {
        QuestionEntity questionEntity = converter.toEntity(req);
        questionEntity.setAgeBracket(ageBracket);
        this.save(questionEntity);
        return questionEntity.getId();
    }

    public void update(Long questionId, Long authorId, UpdateQuestionRequest req) {
        checkCreator(questionId, authorId);
        QuestionEntity updateEntity = converter.toEntity(req);
        this.lambdaUpdate()
                .eq(QuestionEntity::getId, questionId)
                .update(updateEntity);
    }

    public void deleteById(Long questionId, Long userId) {
        checkCreator(questionId, userId);
        this.lambdaUpdate()
                .eq(QuestionEntity::getId, questionId)
                .eq(QuestionEntity::getCreator, userId)
                .remove();
    }

    public QuestionDTO findById(Long questionId) {
        QuestionEntity entity = this.lambdaQuery()
                .eq(QuestionEntity::getId, questionId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(QuestionEntity.class, questionId.toString()));
        QuestionDTO questionDTO = converter.toDto(entity);
        questionDTO.setCreator(userService.getBriefById(entity.getCreator()));
        return questionDTO;
    }

    public AgeBracketEnum findAgeById(Long questionId) {
        QuestionEntity entity = this.lambdaQuery()
                .eq(QuestionEntity::getId, questionId)
                .select(QuestionEntity::getAgeBracket)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(QuestionEntity.class, questionId.toString()));
        return entity.getAgeBracket();
    }

    public void checkCreator(Long questionId, Long userId) {
        QuestionEntity entity = this.lambdaQuery()
                .eq(QuestionEntity::getId, questionId)
                .select(QuestionEntity::getCreator)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(QuestionEntity.class, questionId.toString()));
        if (!entity.getCreator().equals(userId)) {
            throw new NotCreatorException(userId, questionId);
        }
    }

    public PageDTO<QuestionDTO> page(Long cursorId, int size) {
        List<QuestionDTO> records = this.lambdaQuery()
                .gt(QuestionEntity::getId, cursorId)
                .orderByDesc(QuestionEntity::getId)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(e -> {
                    UserBriefDTO userBrief = userService.getBriefById(e.getCreator());
                    QuestionDTO questionDTO = converter.toDto(e);
                    questionDTO.setCreator(userBrief);
                    return questionDTO;
                }).collect(Collectors.toList());
        return PageDTO.<QuestionDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }

    /**
     * 获取用户发布过的问题
     * @param userId
     * @param cursorId
     * @param size
     * @return
     */
    public PageDTO<QuestionDTO> pageByUser(Long userId, Long cursorId, int size) {
        List<QuestionDTO> records = this.lambdaQuery()
                .eq(QuestionEntity::getCreator, userId)
                .gt(QuestionEntity::getId, cursorId)
                .orderByDesc(QuestionEntity::getId)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(e -> {
                    UserBriefDTO userBrief = userService.getBriefById(e.getCreator());
                    QuestionDTO questionDTO = converter.toDto(e);
                    questionDTO.setCreator(userBrief);
                    return questionDTO;
                }).collect(Collectors.toList());
        return PageDTO.<QuestionDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }

    public PageDTO<QuestionDTO> pageByCollection(Long userId, Long cursorId, int size) {
        List<Long> questionIds = collectionRelationService.page(userId, cursorId, size);
        if (CollectionUtils.isEmpty(questionIds)) {
            return PageDTO.<QuestionDTO>builder()
                  .records(Collections.emptyList())
                  .pageSize(size)
                  .build();
        }
        List<QuestionDTO> records = this.lambdaQuery()
                .in(QuestionEntity::getId, questionIds)
                .list()
                .stream()
                .map(e -> {
                    UserBriefDTO userBrief = userService.getBriefById(e.getCreator());
                    QuestionDTO questionDTO = converter.toDto(e);
                    questionDTO.setCreator(userBrief);
                    return questionDTO;
                }).collect(Collectors.toList());
        return PageDTO.<QuestionDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }

    public void afterBeAnswered(Long questionId) {
        QuestionEntity question = this.lambdaQuery()
                .eq(QuestionEntity::getId, questionId)
                .select(QuestionEntity::getTitle, QuestionEntity::getCreator)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(QuestionEntity.class, questionId.toString()));
        question.setId(questionId);
        // 向被回答问题的作者发送通知
        notificationService.publish(
                NotificationType.QUESTION_NEW_ANSWER,
                question,
                question.getTitle()
        );
    }

    /**
     * 收藏
     * @param questionId 被收藏的问题
     * @param userId 收藏的用户
     */
    public void addCollection(Long questionId, Long userId) {
        // 判断问题是否存在，并查询作者和标题
        QuestionEntity question = this.lambdaQuery()
                .eq(QuestionEntity::getId, questionId)
                .select(QuestionEntity::getCreator, QuestionEntity::getTitle)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(QuestionEntity.class, questionId.toString()));
        question.setId(questionId);
        collectionService.collect(userId, questionId);
        notificationService.publish(
                NotificationType.QUESTION_COLLECTED,
                question,
                question.getTitle()
        );
    }
}
