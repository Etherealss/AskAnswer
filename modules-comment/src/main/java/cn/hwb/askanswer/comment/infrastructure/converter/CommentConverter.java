package cn.hwb.askanswer.comment.infrastructure.converter;

import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateReplyRequest;
import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import org.mapstruct.Mapper;
import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface CommentConverter {
    CommentEntity toEntity(CreateCommentRequest req);
    CommentEntity toEntity(CreateReplyRequest req);
    CommentDTO toDto(CommentEntity entity);
}
