package cn.hwb.askanswer.answer.infrastructure.converter;

import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateAnswerRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerAcceptRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerRequest;
import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author hwb
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface AnswerConverter {
    AnswerEntity toEntity(CreateAnswerRequest req);
    AnswerEntity toEntity(UpdateAnswerRequest req);
    AnswerEntity toEntity(UpdateAnswerAcceptRequest req);
    @Mapping(target = "creator", ignore = true)
    AnswerDTO toDto(AnswerEntity entity);
}
