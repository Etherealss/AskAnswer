package cn.hwb.askanswer.comment.infrastructure.validation;

import cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;
import cn.hwb.askanswer.comment.service.comment.CommentService;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.validation.entity.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wtk
 * @date 2023-03-25
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEntityValidator implements EntityValidator {
    private final CommentService commentService;

    @Override
    public void validate(Long id) {
        boolean exist = commentService.lambdaQuery()
                .eq(CommentEntity::getId, id)
                .count() > 0;
        if (!exist) {
            throw new NotFoundException(CommentEntity.class, id.toString());
        }
    }
}
