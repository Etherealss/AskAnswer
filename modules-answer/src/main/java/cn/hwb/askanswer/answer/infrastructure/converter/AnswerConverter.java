package cn.hwb.askanswer.answer.infrastructure.converter;

import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateAnswerRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerAcceptRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerRequest;
import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import org.mapstruct.Mapper;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface AnswerConverter {
    AnswerEntity toEntity(CreateAnswerRequest req);
    AnswerEntity toEntity(UpdateAnswerRequest req);
    AnswerEntity toEntity(UpdateAnswerAcceptRequest req);
    AnswerDTO toDto(AnswerEntity entity);
}
