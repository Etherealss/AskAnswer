package cn.hwb.askanswer.comment.service.comment;

import cn.hwb.askanswer.comment.infrastructure.converter.CommentConverter;
import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;
import cn.hwb.askanswer.comment.mapper.CommentMapper;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CommentService extends ServiceImpl<CommentMapper, CommentEntity> {
    public static final int PAGE_SIZE = 10;

    private final CommentConverter converter;
    private final CommentMapper questionMapper;

    public Long publish(Long targetId, CreateCommentRequest req) {
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

    public CommentDTO findById(Long answerId, Long commentId) {
        CommentEntity entity = this.lambdaQuery()
                .eq(CommentEntity::getId, commentId)
                .one();
        if (entity == null) {
            throw new NotFoundException(CommentEntity.class, commentId.toString());
        }
        if (!entity.getTargetId().equals(answerId)) {
            String format = String.format("'%s'评论的目标并非'%s'",
                    commentId.toString(), answerId.toString());
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

    public PageDTO<CommentDTO> page(int currentPage, int size, Long targetId) {
        IPage<CommentEntity> page = this.lambdaQuery()
                .eq(CommentEntity::getTargetId, targetId)
                .page(new Page<>(currentPage, size));
        return PageDTO.<CommentDTO>builder()
                .currentPage(currentPage)
                .pageSize(size)
                .records(page.getRecords()
                        .stream().map(converter::toDto)
                        .collect(Collectors.toList())
                ).totalPage(page.getTotal() > Integer.MAX_VALUE ?
                        (int) page.getTotal() : Integer.MAX_VALUE
                ).build();
    }

    public PageDTO<CommentDTO> page(Long cursorId, int size, Long targetId) {
        List<CommentDTO> records = this.lambdaQuery()
                .eq(CommentEntity::getTargetId, targetId)
                .gt(CommentEntity::getId, cursorId)
                .orderByDesc(CommentEntity::getId)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return PageDTO.<CommentDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }
}
