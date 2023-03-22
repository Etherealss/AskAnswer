package cn.hwb.askanswer.user.infrastructure.converter;

import cn.hwb.askanswer.user.infrastructure.dto.request.CreateUserRequest;
import cn.hwb.askanswer.user.infrastructure.dto.request.UpdateUserSensitiveInfoRequest;
import cn.hwb.askanswer.user.infrastructure.dto.request.UpdateUserSimpleInfoRequest;
import cn.hwb.askanswer.user.infrastructure.dto.resp.UserBriefDTO;
import cn.hwb.common.base.enums.MapperComponentModel;
import cn.hwb.askanswer.user.service.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface UserConverter {
    UserBriefDTO toBriefDTO(UserEntity user);
    UserEntity toEntity(CreateUserRequest request);
    UserEntity toEntity(UpdateUserSimpleInfoRequest request);
}
