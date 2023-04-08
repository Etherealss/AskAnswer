package cn.hwb.askanswer.comment.service.comment;

import cn.hwb.askanswer.comment.infrastructure.converter.CommentConverter;
import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateReplyRequest;
import cn.hwb.askanswer.comment.mapper.CommentMapper;
import cn.hwb.askanswer.common.base.enums.NotificationType;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.like.infrastructure.enums.LikeTargetType;
import cn.hwb.askanswer.like.service.LikeRelationService;
import cn.hwb.askanswer.like.service.LikeService;
import cn.hwb.askanswer.notification.service.NotificationService;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserPageDTO;
import cn.hwb.askanswer.user.service.user.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hwb
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService extends ServiceImpl<CommentMapper, CommentEntity> {
    public static final int PAGE_SIZE = 10;

    private final CommentConverter converter;
    private final CommentMapper questionMapper;
    private final UserService userService;
    private final LikeRelationService likeRelationService;
    private final LikeService likeService;
    private final NotificationService notificationService;

    /**
     * 发布评论
     * @param targetId
     * @param req
     * @return
     */
    public Long publishComment(Long targetId, CreateCommentRequest req) {
        CommentEntity commentEntity = converter.toEntity(req);
        commentEntity.setTargetId(targetId);
        commentEntity.setContainerId(targetId);
        this.save(commentEntity);
        return commentEntity.getId();
    }

    /**
     * 发布回复
     * @param targetId
     * @param req
     * @return
     */
    public Long publishReply(Long targetId, CreateReplyRequest req) {
        // 检查被回复的评论是否存在
        CommentEntity targetComment = this.lambdaQuery()
                .eq(CommentEntity::getId, targetId)
                .select(CommentEntity::getCreator)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(CommentEntity.class, targetId.toString()));
        targetComment.setId(targetId);
        CommentEntity commentEntity = converter.toEntity(req);
        commentEntity.setTargetId(targetId);
        this.save(commentEntity);
        // 发送"评论被回复"通知
        this.sendNotification(targetComment);
        return commentEntity.getId();
    }

    private void sendNotification(CommentEntity commentEntity) {
        // 向被评论的用户发送通知
        notificationService.publish(
                NotificationType.COMMENT_NEW_REPLY,
                commentEntity,
                "评论"
        );
    }

    public void deleteById(Long commentId, Long curUserId) {
        checkExistAndCreator(commentId, curUserId);
        this.lambdaUpdate()
                .eq(CommentEntity::getId, commentId)
                .eq(CommentEntity::getCreator, curUserId)
                .remove();
    }

    /**
     * 获取评论
     * @param targetId 被评论的对象，用于检查
     * @param commentId 评论ID
     * @return
     */
    public CommentDTO findById(Long targetId, Long commentId) {
        CommentEntity entity = this.lambdaQuery()
                .eq(CommentEntity::getId, commentId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(CommentEntity.class, commentId.toString()));
        if (!entity.getTargetId().equals(targetId)) {
            String format = String.format("'%s'评论的目标并非'%s'",
                    commentId.toString(), targetId.toString());
            throw new BadRequestException(ResultCode.COMMENT_TARGET_NOT_MATCH, format);
        }
        return converter.toDto(entity);
    }

    private void checkExistAndCreator(Long commentId, Long userId) {
        CommentEntity entity = this.lambdaQuery()
                .eq(CommentEntity::getId, commentId)
                .select(CommentEntity::getCreator)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(CommentEntity.class, commentId.toString()));
        if (!entity.getCreator().equals(userId)) {
            throw new NotCreatorException(userId, commentId);
        }
    }

    /**
     * 分页获取评论
     * @param currentPage 当前页
     * @param size 页面显示的评论数
     * @param targetId 获取评论时，传被评论的对象ID；获取评论的回复时，传评论ID
     * @param isSubReply 是否获取回复？
     * 如果为true，则会获得盖楼式的回复（例如评论23回复了评论1，那么会把1和23都查出来）
     * 如果是false，则会按照评论的发布顺序获取信息（按顺序就是1、2、3……，即使23回复了1也不会被查出来）
     * @return
     */
    public UserPageDTO<CommentDTO> page(int currentPage, int size, Long targetId, boolean isSubReply) {
        LambdaQueryChainWrapper<CommentEntity> lambdaQuery = this.lambdaQuery();
        if (isSubReply) {
            // 获取回复了 targetId 的信息
            lambdaQuery.eq(CommentEntity::getContainerId, targetId);
        } else {
            // 获取评论了 targetId 的信息
            lambdaQuery.eq(CommentEntity::getTargetId, targetId);
        }
        IPage<CommentEntity> page = lambdaQuery.page(new Page<>(currentPage, size));
        List<CommentDTO> records = page.getRecords()
                .stream().map(converter::toDto)
                .collect(Collectors.toList());
        Map<Long, UserBriefDTO> userMap = records.stream()
                .map(CommentDTO::getCreator)
                .distinct() // 去重
                .map(userService::getBriefById)
                .collect(Collectors.toMap(UserBriefDTO::getId, Function.identity()));
        return new UserPageDTO<>(records, page, userMap);
    }

    public UserPageDTO<CommentDTO> page(Long cursorId, int size, Long targetId) {
        List<CommentDTO> records = this.lambdaQuery()
                .eq(CommentEntity::getTargetId, targetId)
                .gt(CommentEntity::getId, cursorId)
                .orderByDesc(CommentEntity::getId)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        UserPageDTO<CommentDTO> page = new UserPageDTO<>();
        page.setRecords(records);
        page.setPageSize(size);
        Map<Long, UserBriefDTO> userMap = records.stream()
                .map(CommentDTO::getCreator)
                .distinct() // 去重
                .map(userService::getBriefById)
                .collect(Collectors.toMap(UserBriefDTO::getId, Function.identity()));
        page.setUserMap(userMap);
        return page;
    }

    public UserPageDTO<CommentDTO> pageByLike(Long userId, Long cursorId, int size) {
        List<Long> commentIds = likeRelationService.page(userId, cursorId, size, LikeTargetType.COMMENT);
        List<CommentDTO> records;
        if (CollectionUtils.isEmpty(commentIds)) {
            records = Collections.emptyList();
        } else {
            records = this.lambdaQuery()
                    .in(CommentEntity::getId, commentIds)
                    .orderByDesc(CommentEntity::getId)
                    .list()
                    .stream()
                    .map(converter::toDto)
                    .collect(Collectors.toList());
        }
        UserPageDTO<CommentDTO> page = new UserPageDTO<>();
        page.setRecords(records);
        page.setPageSize(size);
        Map<Long, UserBriefDTO> userMap = records.stream()
                .map(CommentDTO::getCreator)
                .distinct() // 去重
                .map(userService::getBriefById)
                .collect(Collectors.toMap(UserBriefDTO::getId, Function.identity()));
        page.setUserMap(userMap);
        return page;
    }

    public PageDTO<CommentDTO> pageByUser(Long userId, Long cursorId, int size) {
        List<CommentDTO> records  = this.lambdaQuery()
                    // 只获取某一个用户的评论
                    .eq(CommentEntity::getCreator, userId)
                    .orderByDesc(CommentEntity::getId)
                    .list()
                    .stream()
                    .map(converter::toDto)
                    .collect(Collectors.toList());
        return PageDTO.<CommentDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }

    public void like(Long userId, Long commentId) {
        // 检查评论是否存在，并获取通知信息
        CommentEntity commentEntity = this.lambdaQuery()
                .eq(CommentEntity::getId, commentId)
                .select(CommentEntity::getCreator)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(CommentEntity.class, commentId.toString()));
        commentEntity.setId(commentId);
        likeService.like(userId, commentId, LikeTargetType.COMMENT);
        // 向被点赞的用户发送通知
        notificationService.publish(
                NotificationType.COMMENT_NEW_LIKE,
                commentEntity,
                "评论"
        );
    }
}
