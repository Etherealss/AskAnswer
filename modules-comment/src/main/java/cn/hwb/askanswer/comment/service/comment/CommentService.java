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
import cn.hwb.askanswer.common.base.pojo.vo.NotificationTemplate;
import cn.hwb.askanswer.common.base.utils.IntegerUtil;
import cn.hwb.askanswer.like.infrastructure.enums.LikeTargetType;
import cn.hwb.askanswer.like.service.LikeRelationService;
import cn.hwb.askanswer.notification.service.NotificationService;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserPageDTO;
import cn.hwb.askanswer.user.service.user.UserService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
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
    private final NotificationService notificationService;

    public Long publish(Long targetId, CreateCommentRequest req) {
        CommentEntity commentEntity = converter.toEntity(req);
        commentEntity.setTargetId(targetId);
        commentEntity.setContainerId(targetId);
        this.save(commentEntity);
        return commentEntity.getId();
    }

    public Long publish(Long targetId, CreateReplyRequest req) {
        CommentEntity targetComment = this.lambdaQuery()
                .eq(CommentEntity::getTargetId, targetId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(CommentEntity.class, targetId.toString()));
        CommentEntity commentEntity = converter.toEntity(req);
        commentEntity.setTargetId(targetId);
        this.save(commentEntity);
        this.sendNotification(targetComment);
        return commentEntity.getId();
    }

    private void sendNotification(CommentEntity commentBe) {
        // 填充"新评论"通知的相关信息
        NotificationTemplate notificationTemplate = new NotificationTemplate()
                .setTargetId(commentBe.getId())
                .setTargetDesc("评论")
                .setUserId(UserSecurityContextHolder.require().getUserId())
                .setUsername(UserSecurityContextHolder.require().getUsername());
        // 向被评论的用户发送通知
        notificationService.publish(
                NotificationType.COMMENT_NEW_LIKE,
                commentBe,
                "评论"
        );
    }

    public void deleteById(Long commentId, Long curUserId) {
        checkCreator(commentId, curUserId);
        this.lambdaUpdate()
                .eq(CommentEntity::getId, commentId)
                .eq(CommentEntity::getCreator, curUserId)
                .remove();
    }

    public CommentDTO findById(Long targetId, Long commentId) {
        CommentEntity entity = this.lambdaQuery()
                .eq(CommentEntity::getId, commentId)
                .one();
        if (entity == null) {
            throw new NotFoundException(CommentEntity.class, commentId.toString());
        }
        if (!entity.getTargetId().equals(targetId)) {
            String format = String.format("'%s'评论的目标并非'%s'",
                    commentId.toString(), targetId.toString());
            throw new BadRequestException(ResultCode.COMMENT_TARGET_NOT_MATCH, format);
        }
        return converter.toDto(entity);
    }

    private void checkCreator(Long commentId, Long userId) {
        CommentEntity entity = this.lambdaQuery()
                .eq(CommentEntity::getId, commentId)
                .select(CommentEntity::getCreator).one();
        if (entity == null) {
            throw new NotFoundException(CommentEntity.class, commentId.toString());
        }
        if (!entity.getCreator().equals(userId)) {
            throw new NotCreatorException(userId, commentId);
        }
    }

    public UserPageDTO<CommentDTO> page(int currentPage, int size, Long targetId, boolean subReply) {
        LambdaQueryChainWrapper<CommentEntity> lambdaQuery = this.lambdaQuery();
        if (subReply) {
            lambdaQuery.eq(CommentEntity::getContainerId, targetId);
        } else {
            lambdaQuery.eq(CommentEntity::getTargetId, targetId);
        }
        IPage<CommentEntity> page = lambdaQuery
                .page(new Page<>(currentPage, size));
        List<CommentDTO> records = page.getRecords()
                .stream().map(converter::toDto)
                .collect(Collectors.toList());
        Map<Long, UserBriefDTO> userMap = records.stream()
                .map(CommentDTO::getCreator)
                .distinct() // 去重
                .map(userService::getBriefById)
                .collect(Collectors.toMap(UserBriefDTO::getId, Function.identity()));
        UserPageDTO<CommentDTO> userPageDTO = new UserPageDTO<>();
        userPageDTO.setRecords(records);
        userPageDTO.setPageSize(size);
        userPageDTO.setCurrentPage(currentPage);
        userPageDTO.setTotalPage(IntegerUtil.long2Int(page.getPages()));
        userPageDTO.setTotalSize(IntegerUtil.long2Int(page.getTotal()));
        userPageDTO.setUserMap(userMap);
        return userPageDTO;
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
}
