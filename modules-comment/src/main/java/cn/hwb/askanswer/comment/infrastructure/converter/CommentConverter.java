package cn.hwb.askanswer.comment.infrastructure.converter;

import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateReplyRequest;
import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import org.mapstruct.Mapper;
import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;
import org.mapstruct.Mapping;

/**
 * @author hwb
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface CommentConverter {
    CommentEntity toEntity(CreateCommentRequest req);
    CommentEntity toEntity(CreateReplyRequest req);
    @Mapping(target = "creator", ignore = true)
    CommentDTO toDto(CommentEntity entity);
}
