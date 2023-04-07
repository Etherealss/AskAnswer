package cn.hwb.askanswer.user.infrastructure.converter;

import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserAuthDTO;
import cn.hwb.askanswer.user.infrastructure.pojo.request.CreateUserRequest;
import cn.hwb.askanswer.user.infrastructure.pojo.request.UpdateUserInfoRequest;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import cn.hwb.askanswer.user.infrastructure.pojo.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * @author hwb
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface UserConverter {
    UserBriefDTO toBriefDTO(UserEntity user);
    UserAuthDTO toAuthDTO(UserEntity user);
    UserEntity toEntity(CreateUserRequest request);
    UserEntity toEntity(UpdateUserInfoRequest request);
}
