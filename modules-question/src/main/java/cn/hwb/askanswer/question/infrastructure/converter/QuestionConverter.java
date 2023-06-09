package cn.hwb.askanswer.question.infrastructure.converter;

import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import cn.hwb.askanswer.question.infrastructure.pojo.dto.QuestionDTO;
import cn.hwb.askanswer.question.infrastructure.pojo.entity.QuestionEntity;
import cn.hwb.askanswer.question.infrastructure.pojo.request.CreateQuestionRequest;
import cn.hwb.askanswer.question.infrastructure.pojo.request.UpdateQuestionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author hwb
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface QuestionConverter {
    QuestionEntity toEntity(CreateQuestionRequest req);
    QuestionEntity toEntity(UpdateQuestionRequest req);
    @Mapping(target = "creator", ignore = true)
    QuestionDTO toDto(QuestionEntity entity);
}
