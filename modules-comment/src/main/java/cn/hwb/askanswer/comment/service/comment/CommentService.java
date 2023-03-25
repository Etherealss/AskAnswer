package cn.hwb.askanswer.comment.service.comment;

import cn.hwb.askanswer.comment.infrastructure.converter.CommentConverter;
import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateReplyRequest;
import cn.hwb.askanswer.comment.mapper.CommentMapper;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.utils.IntegerUtil;
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

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService extends ServiceImpl<CommentMapper, CommentEntity> {
    public static final int PAGE_SIZE = 10;

    private final CommentConverter converter;
    private final CommentMapper questionMapper;
    private final UserService userService;

    public Long publish(Long targetId, CreateCommentRequest req) {
        CommentEntity commentEntity = converter.toEntity(req);
        commentEntity.setTargetId(targetId);
        commentEntity.setContainerId(targetId);
        this.save(commentEntity);
        return commentEntity.getId();
    }

    public Long publish(Long targetId, CreateReplyRequest req) {
        CommentEntity commentEntity = converter.toEntity(req);
        commentEntity.setTargetId(targetId);
        this.save(commentEntity);
        return commentEntity.getId();
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
                .distinct()
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
                .distinct()
                .map(userService::getBriefById)
                .collect(Collectors.toMap(UserBriefDTO::getId, Function.identity()));
        return page;
    }
}
