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
import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import cn.hwb.askanswer.common.base.enums.NotificationType;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.pojo.event.AnswerAgeLimitEvent;
import cn.hwb.askanswer.common.base.pojo.event.AnswerPublishedEvent;
import cn.hwb.askanswer.common.base.pojo.event.QuestionCreatorValidateEvent;
import cn.hwb.askanswer.like.infrastructure.enums.LikeTargetType;
import cn.hwb.askanswer.like.service.LikeRelationService;
import cn.hwb.askanswer.like.service.LikeService;
import cn.hwb.askanswer.notification.service.NotificationService;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.service.user.UserService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class AnswerService extends ServiceImpl<AnswerMapper, AnswerEntity> {
    public static final int PAGE_SIZE = 10;

    private final AnswerConverter converter;
    private final AnswerMapper questionMapper;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final LikeRelationService likeRelationService;
    private final LikeService likeService;
    private final NotificationService notificationService;
    private final CommentService commentService;

    /**
     * 发布回答
     * @param questionId
     * @param userId
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long publish(Long questionId, Long userId, AgeBracketEnum ageBracket,
                        CreateAnswerRequest req) {
        // 验证年龄段是否可以回答问题
        eventPublisher.publishEvent(new AnswerAgeLimitEvent(questionId, userId, ageBracket));
        // 保存回答
        AnswerEntity answerEntity = converter.toEntity(req);
        answerEntity.setQuestionId(questionId);
        this.save(answerEntity);
        Long answerId = answerEntity.getId();
        eventPublisher.publishEvent(new AnswerPublishedEvent(questionId, userId));
        return answerId;
    }

    /**
     * 更新回答
     * @param questionId
     * @param answerId
     * @param answerCreator
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Long questionId, Long answerId, Long answerCreator, UpdateAnswerRequest req) {
        preCheck(questionId, answerId, answerCreator);
        AnswerEntity updateEntity = converter.toEntity(req);
        boolean update = this.lambdaUpdate()
                // id = answerId；eq就是equals的意思
                .eq(AnswerEntity::getId, answerId)
                .eq(AnswerEntity::getQuestionId, questionId)
                .update(updateEntity);
    }

    /**
     * 问题作者采纳回答
     * @param questionId
     * @param answerId
     * @param questionCreator
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Long questionId, Long answerId, Long questionCreator,
                       UpdateAnswerAcceptRequest req) {
        // 验证当前用户是否为问题作者
        eventPublisher.publishEvent(
                new QuestionCreatorValidateEvent(questionId, questionCreator)
        );
        // 获取回答信息，以检查回答是否存在以及回答的作者的信息是否匹配
        AnswerEntity answer = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .select(AnswerEntity::getQuestionId, AnswerEntity::getCreator, AnswerEntity::getTitle, AnswerEntity::getIsAccepted)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, questionId.toString()));
        preCheck(null, answerId, null, answer);
        if (answer.getIsAccepted()) {
            throw new BadRequestException(ResultCode.ANSWER_ALREADY_ACCEPTED, "回答" + answerId + "已被采纳");
        }
        answer.setId(answerId);
        this.lambdaUpdate()
                .eq(AnswerEntity::getId, answerId)
                .eq(AnswerEntity::getQuestionId, questionId)
                // 更新采纳状态
                .set(AnswerEntity::getIsAccepted, req.getIsAccepted())
                .update();
        if (req.getIsAccepted()) {
            // 回答被采纳通知
            notificationService.publish(
                    NotificationType.ANSWER_ACCEPTED,
                    answer,
                    answer.getTitle()
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long answerId, Long userId) {
        preCheck(null, answerId, userId);
        this.lambdaUpdate()
                .eq(AnswerEntity::getId, answerId)
                // 只有作者才可以删除自己的回答
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
        // 检查questionId对应的问题是否存在
        AnswerEntity entity = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .select(AnswerEntity::getQuestionId, AnswerEntity::getCreator)
                .oneOpt()
                // 不存在就抛异常
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, questionId.toString()));
        preCheck(questionId, answerId, answerCreator, entity);
    }

    private void preCheck(Long questionId, Long answerId, Long answerCreator, AnswerEntity entity) {
        // 验证作者
        if (answerCreator != null && !answerCreator.equals(entity.getCreator())) {
            throw new NotCreatorException(answerCreator, answerId);
        }
        // 验证问题与回答是否匹配。如果不匹配，说明前端参数传输错误，是一个bug，需要抛出异常。
        if (questionId != null && !questionId.equals(entity.getQuestionId())) {
            String format = String.format("'%s'回答的问题并非'%s'",
                    answerId.toString(), questionId.toString());
            throw new BadRequestException(ResultCode.ANSWER_TARGET_NOT_MATCH, format);
        }
    }

    /**
     * 分页获取问题的回答
     * @param cursorId 游标ID
     * @param size 页面显示的回答数
     * @param questionId 回答的问题的ID
     * @return
     */
    public PageDTO<AnswerDTO> pageByQuestion(Long cursorId, int size, Long questionId) {
        return this.page(cursorId, size, questionId, null);
    }

    /**
     * 分页获取用户的回答，不局限于某个问题之下
     * @param cursorId 游标ID
     * @param size 页面显示的回答数
     * @param userId 用户ID
     * @return
     */
    public PageDTO<AnswerDTO> pageByUser(Long cursorId, int size, Long userId) {
        return this.page(cursorId, size, null, userId);
    }

    /**
     * 分页获取回答
     * @param cursorId 游标ID
     * @param size 页面显示的回答数
     * @param questionId 回答的问题的ID。允许为空
     * @param creatorId 回答作者ID。允许为空
     * @return
     */
    private PageDTO<AnswerDTO> page(Long cursorId, int size, @Nullable Long questionId, @Nullable Long creatorId) {
        LambdaQueryChainWrapper<AnswerEntity> wrapper = this.lambdaQuery();
        if (questionId != null) {
            wrapper.eq(AnswerEntity::getQuestionId, questionId);
        }
        if (creatorId != null) {
            wrapper.eq(AnswerEntity::getCreator, creatorId);
        }
        List<AnswerEntity> answerEntities = wrapper
                // 查询比游标 cursorId 更小的 ID
                .lt(AnswerEntity::getId, cursorId)
                // 按 ID 排序(order)
                .orderByDesc(AnswerEntity::getId)
                // 限制查询的记录数量，只查前 size 条
                .last(String.format("LIMIT %d", size))
                .list();
        List<AnswerDTO> records = answerEntities.stream()
                // 匿名处理
                .map(this::anonymousHandle)
                // 根据 Long creator 字段获取用户信息，添加到 AnswerDTO 中
                .map(this::addUserAndToAnswerDTO)
                .collect(Collectors.toList());
        // 构造分页对象
        return PageDTO.<AnswerDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }

    /**
     * 根据 Long creator 字段获取用户信息，添加到 AnswerDTO 中
     * @param e
     * @return
     */
    private AnswerDTO addUserAndToAnswerDTO(AnswerEntity e) {
        AnswerDTO answerDTO = converter.toDto(e);
        // 匿名处理过的entity没有creator字段
        if (e.getCreator() != null) {
            UserBriefDTO userBrief = userService.getBriefById(e.getCreator());
            answerDTO.setCreator(userBrief);
        }
        return answerDTO;
    }

    public PageDTO<AnswerDTO> pageByLikes(Long userId, Long cursorId, int size) {
        List<Long> answerIds = likeRelationService.page(userId, cursorId, size, LikeTargetType.ANSWER);
        // 用户没有点赞的回答
        if (CollectionUtils.isEmpty(answerIds)) {
            return PageDTO.<AnswerDTO>builder()
                    .records(Collections.emptyList())
                    .pageSize(size)
                    .build();
        }
        // 根据用户点赞的回答批量获取回答信息
        List<AnswerDTO> records = this.lambdaQuery()
                .in(AnswerEntity::getId, answerIds)
                .orderByDesc(AnswerEntity::getId)
                .list()
                .stream()
                .map(this::anonymousHandle)
                // 根据 Long creator 字段获取用户信息，添加到 AnswerDTO 中
                .map(this::addUserAndToAnswerDTO)
                .collect(Collectors.toList());
        return PageDTO.<AnswerDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void answerBeLiked(Long answerId, Long likeUserId) {
        // 先判断问题是否存在
        AnswerEntity answer = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .select(AnswerEntity::getCreator, AnswerEntity::getTitle)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, answerId.toString()));
        answer.setId(answerId);
        // 问题存在，点赞
        likeService.like(likeUserId, answerId, LikeTargetType.ANSWER);
        // 向被点赞的回答作者发送通知
        notificationService.publish(
                NotificationType.ANSWER_NEW_LIKE,
                answer,
                answer.getTitle()
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

    @Transactional(rollbackFor = Exception.class)
    public Long publishComment(Long answerId, CreateCommentRequest req) {
        // 判断问题是否存在，并查询问题的作者与标题，用于填充到通知里
        AnswerEntity answer = this.lambdaQuery()
                .eq(AnswerEntity::getId, answerId)
                .select(AnswerEntity::getCreator, AnswerEntity::getTitle)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(AnswerEntity.class, answerId.toString()));
        answer.setId(answerId);
        Long commentId = commentService.publishComment(answerId, req);
        // 回答被评论通知
        notificationService.publish(
                NotificationType.ANSWER_NEW_COMMENT,
                answer,
                answer.getTitle()
        );
        return commentId;
    }
}
