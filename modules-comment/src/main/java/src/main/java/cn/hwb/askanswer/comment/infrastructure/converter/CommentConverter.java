package src.main.java.cn.hwb.askanswer.comment.infrastructure.converter;

import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import org.mapstruct.Mapper;
import src.main.java.cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import src.main.java.cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;
import src.main.java.cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;
import src.main.java.cn.hwb.askanswer.comment.infrastructure.pojo.request.UpdateCommentRequest;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface CommentConverter {
    CommentEntity toEntity(CreateCommentRequest req);
    CommentEntity toEntity(UpdateCommentRequest req);
    CommentDTO toDto(CommentEntity entity);
}
