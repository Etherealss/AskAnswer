package cn.hwb.askanswer.user.infrastructure.converter;

import cn.hwb.askanswer.user.infrastructure.pojo.request.CreateUserRequest;
import cn.hwb.askanswer.user.infrastructure.pojo.request.UpdateUserSimpleInfoRequest;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.common.base.enums.MapperComponentModel;
import cn.hwb.askanswer.user.infrastructure.pojo.entity.UserEntity;
import org.mapstruct.Mapper;

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
