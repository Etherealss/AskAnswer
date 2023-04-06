package cn.hwb.askanswer.answer.service.answer;

import cn.hwb.askanswer.answer.infrastructure.converter.AnswerConverter;
import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateAnswerRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerAcceptRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerRequest;
import cn.hwb.askanswer.answer.mapper.AnswerMapper;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;
import cn.hwb.askanswer.comment.service.comment.CommentService;
import cn.hwb.askanswer.common.base.enums.NotificationTargetType;
import cn.hwb.askanswer.common.base.enums.NotificationType;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.pojo.event.QuestionCreatorValidateEvent;
import cn.hwb.askanswer.like.infrastructure.enums.LikeTargetType;
import cn.hwb.askanswer.like.service.LikeRelationService;
import cn.hwb.askanswer.like.service.LikeService;
import cn.hwb.askanswer.notification.infrastructure.pojo.request.PublishNotificationRequest;
import cn.hwb.askanswer.notification.infrastructure.pojo.vo.NotificationTemplate;
import cn.hwb.askanswer.notification.service.NotificationService;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.common.security.agelimit.AgeLimitVerifier;
import cn.hwb.common.security.auth.exception.AgeLimitedException;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
    private final LikeRelationService likeRelationService;
    private final LikeService likeService;
    private final NotificationService notificationService;
    private final CommentService commentService;

    public Long publish(Long questionId, CreateAnswerRequest req) {
        boolean verify = ageLimitVerifier.verify(questionId);
        if (!verify) {
            throw new AgeLimitedException("当前用户年龄段受限");
        }
        AnswerEntity answerEntity = converter.toEntity(req);
        answerEntity.setQuestionId(questionId);
        this.save(answerEntity);
        Long answerId = answerEntity.getId();

        return answerId;
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

    public AnswerDTO findById(Long answerId) {
        AnswerEntity entity = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, answerId.toString()));
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

    public PageDTO<AnswerDTO> pageByLikes(Long userId, Long cursorId, int size) {
        List<Long> answerIds = likeRelationService.page(userId, cursorId, size, LikeTargetType.ANSWER);
        if (CollectionUtils.isEmpty(answerIds)) {
            return PageDTO.<AnswerDTO>builder()
                    .records(Collections.emptyList())
                    .pageSize(size)
                    .build();
        }
        List<AnswerDTO> records = this.lambdaQuery()
                .in(AnswerEntity::getId, answerIds)
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

    public void answerBeLiked(Long answerId, Long likeUserId) {
        // 先判断问题是否存在
        AnswerEntity answer = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .select(AnswerEntity::getCreator, AnswerEntity::getTitle)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, answerId.toString()));
        // 问题存在，点赞
        likeService.like(likeUserId, answerId, LikeTargetType.ANSWER);
        // 填充点赞通知的相关信息
        NotificationTemplate notificationTemplate = new NotificationTemplate()
                .setTargetId(answerId)
                .setTargetDesc(answer.getTitle())
                .setTargetType(NotificationTargetType.ANSWER.getCode())
                .setUserId(likeUserId)
                .setUsername(UserSecurityContextHolder.require().getUsername());
        // 向被点赞的回答作者发送通知
        notificationService.publish(new PublishNotificationRequest()
                .setProps(notificationTemplate)
                .setType(NotificationType.LIKE)
                .setRcvrId(answer.getCreator())
        );
    }

    /**
     * 匿名回答需要隐藏作者
     * @param entity
     * @return
     */
    private AnswerEntity anonymousHandle(AnswerEntity entity) {
        if (entity.getIsAnonymous()) {
            entity.setCreator(null);
        }
        return entity;
    }

    public Long publishComment(Long answerId, CreateCommentRequest req) {
        // 判断问题是否存在，并查询问题的作者与标题，用于填充到通知里
        AnswerEntity answer = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .select(AnswerEntity::getCreator, AnswerEntity::getTitle)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, answerId.toString()));
        Long commentId = commentService.publish(answerId, req);
        // 填充评论通知的相关信息
        NotificationTemplate template = new NotificationTemplate()
                .setTargetId(answerId)
                .setTargetDesc(answer.getTitle())
                .setTargetType(NotificationTargetType.ANSWER.getCode())
                .setUsername(UserSecurityContextHolder.require().getUsername())
                .setUserId(answer.getCreator());
        // 向被评论的回答作者发送通知
        notificationService.publish(new PublishNotificationRequest()
                .setProps(template)
                .setType(NotificationType.COMMENT)
                .setRcvrId(answer.getCreator())
        );
        return commentId;
    }
}
