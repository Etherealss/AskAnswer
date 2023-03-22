package cn.hwb.askanswer.user.service.user;

import cn.hwb.askanswer.user.infrastructure.converter.UserConverter;
import cn.hwb.askanswer.user.infrastructure.dto.request.CreateUserRequest;
import cn.hwb.askanswer.user.infrastructure.dto.request.UpdateUserSensitiveInfoRequest;
import cn.hwb.askanswer.user.infrastructure.dto.request.UpdateUserSimpleInfoRequest;
import cn.hwb.askanswer.user.infrastructure.dto.resp.UserBriefDTO;
import cn.hwb.askanswer.user.service.user.avatar.UserAvatarService;
import cn.hwb.common.base.crypt.PasswordEncryptor;
import cn.hwb.common.base.enums.ResultCode;
import cn.hwb.common.base.exception.BaseException;
import cn.hwb.common.base.exception.rest.ParamMissingException;
import cn.hwb.common.base.exception.service.ExistException;
import cn.hwb.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.user.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserService extends ServiceImpl<UserMapper, UserEntity> {
    private final UserMapper userMapper;
    private final UserConverter userConverter;
    private final UserAvatarService userAvatarService;
    private final PasswordEncryptor passwordEncryptor;

    public Long createUser(CreateUserRequest request) {
        if (this.checkUsernameExists(request.getUsername())) {
            throw new ExistException(UserEntity.class, request.getUsername());
        }
        UserEntity userEntity = userConverter.toEntity(request);
        userEntity.setAvatar(userAvatarService.getDefaultAvatar());
        userEntity.setPassword(passwordEncryptor.encode(userEntity.getPassword()));
        this.save(userEntity);
        return userEntity.getId();
    }

    public UserBriefDTO getBriefById(Long userId) {
        UserEntity user = lambdaQuery().eq(UserEntity::getId, userId).one();
        if (user == null) {
            throw new NotFoundException(UserEntity.class, userId.toString());
        }
        return userConverter.toBriefDTO(user);
    }

    public List<UserBriefDTO> getBatchBriefsByIds(Collection<Long> userIds) {
        List<UserEntity> users = userMapper.selectBatchIds(userIds);
        return users.stream()
                .map(userConverter::toBriefDTO)
                .collect(Collectors.toList());
    }

    public boolean checkUsernameExists(String username) {
        return lambdaQuery().eq(UserEntity::getUsername, username).count() > 0;
    }

    public void update(Long userId, UpdateUserSimpleInfoRequest req) {
        UserEntity user = lambdaQuery().eq(UserEntity::getId, userId).one();
        if (user == null) {
            throw new NotFoundException(UserEntity.class, userId.toString());
        }
        String newPassword = req.getNewPassword();
        if (StringUtils.hasText(newPassword) || req.getBirthday() != null) {
            if (!StringUtils.hasText(req.getCurPassword())) {
                throw new ParamMissingException("curPassword", "修改敏感信息需要提供密码");
            }
            if (!passwordEncryptor.match(req.getCurPassword(), user.getPassword())) {
                throw new BaseException(ResultCode.PASSWORD_NOT_MATCH);
            }
        }
        UserEntity update = userConverter.toEntity(req);
        if (StringUtils.hasText(newPassword)) {
            update.setPassword(newPassword);
        }
        update.setId(userId);
        this.saveOrUpdate(update);
    }

}
