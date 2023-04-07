package cn.hwb.askanswer.user.service.user;

import cn.hwb.askanswer.common.base.crypt.PasswordEncryptor;
import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import cn.hwb.askanswer.common.base.exception.rest.ParamMissingException;
import cn.hwb.askanswer.common.base.exception.service.ExistException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.file.domain.FileUploadDTO;
import cn.hwb.askanswer.user.infrastructure.converter.UserConverter;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserAuthDTO;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import cn.hwb.askanswer.user.infrastructure.pojo.entity.UserEntity;
import cn.hwb.askanswer.user.infrastructure.pojo.request.CreateUserRequest;
import cn.hwb.askanswer.user.infrastructure.pojo.request.UpdateUserSimpleInfoRequest;
import cn.hwb.askanswer.user.mapper.UserMapper;
import cn.hwb.askanswer.user.service.user.avatar.UserAvatarService;
import cn.hwb.askanswer.user.service.user.review.UserReviewService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hwb
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserService extends ServiceImpl<UserMapper, UserEntity> {
    private final UserMapper userMapper;
    private final UserConverter userConverter;
    private final UserAvatarService userAvatarService;
    private final UserReviewService userReviewService;
    private final PasswordEncryptor passwordEncryptor;

    public Long createUser(CreateUserRequest request) {
        if (this.checkUsernameExists(request.getUsername())) {
            throw new ExistException(UserEntity.class, request.getUsername());
        }
        UserEntity userEntity = userConverter.toEntity(request)
                .setAvatar(userAvatarService.getDefaultAvatar())
                .setRoles(new ArrayList<>(0))
                .setIsReviewed(false)
                .setReviewImg("");
        userEntity.setPassword(passwordEncryptor.encode(userEntity.getPassword()));
        this.save(userEntity);
        return userEntity.getId();
    }

    public UserBriefDTO getBriefById(Long userId) {
        UserEntity user = lambdaQuery()
                .eq(UserEntity::getId, userId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(UserEntity.class, userId.toString()));
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
        UserEntity user = lambdaQuery()
                .eq(UserEntity::getId, userId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(UserEntity.class, userId.toString()));
        String newPassword = req.getNewPassword();
        if (StringUtils.hasText(newPassword) || req.getBirthday() != null) {
            if (!StringUtils.hasText(req.getCurPassword())) {
                throw new ParamMissingException("curPassword", "修改敏感信息需要提供密码");
            }
            if (!passwordEncryptor.match(req.getCurPassword(), user.getPassword())) {
                throw new BadRequestException(ResultCode.PASSWORD_NOT_MATCH);
            }
        }
        UserEntity update = userConverter.toEntity(req);
        if (StringUtils.hasText(newPassword)) {
            update.setPassword(newPassword);
        }
        update.setId(userId);
        this.saveOrUpdate(update);
    }

    public String updateAvatar(Long userId, MultipartFile file) {
        FileUploadDTO fileUploadDTO = userAvatarService.uploadAvatar(file, userId);
        String url = fileUploadDTO.getUrl();
        boolean update = this.lambdaUpdate()
                .eq(UserEntity::getId, userId)
                .set(UserEntity::getAvatar, url)
                .update();
        if (!update) {
            // 有Token校验在前，此处一般不会NotFound
            throw new NotFoundException(UserEntity.class, userId.toString());
        }
        return url;
    }

    public void updateReviewImg(Long userId, MultipartFile file) {
        FileUploadDTO fileUploadDTO = userReviewService.uploadAvatar(userId, file);
        String reviewImgUrl = fileUploadDTO.getUrl();
        boolean update = this.lambdaUpdate()
                .eq(UserEntity::getId, userId)
                .set(UserEntity::getReviewImg, reviewImgUrl)
                .update();
        if (!update) {
            throw new NotFoundException(UserEntity.class, userId.toString());
        }
    }

    public void reviewPass(Long userId) {
        boolean update = this.lambdaUpdate()
                .eq(UserEntity::getId, userId)
                .set(UserEntity::getIsReviewed, true)
                .update();
        if (!update) {
            throw new NotFoundException(UserEntity.class, userId.toString());
        }
    }

    public void reviewFail(Long userId) {
        userMapper.deleteById(userId);
    }

    public String getReviewImg(Long userId) {
        UserEntity entity = this.lambdaQuery()
                .eq(UserEntity::getId, userId)
                .select(UserEntity::getReviewImg)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(UserEntity.class, userId.toString()));
        return entity.getReviewImg();
    }

    public UserAuthDTO get4Review(Long userId) {
        UserEntity entity = this.lambdaQuery()
                .eq(UserEntity::getId, userId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(UserEntity.class, userId.toString()));
        return userConverter.toAuthDTO(entity);
    }

    public PageDTO<UserAuthDTO> page4Review(int currentPage, int size) {
        Page<UserEntity> page = lambdaQuery()
                .eq(UserEntity::getIsReviewed, false)
                .page(new Page<>(currentPage, size));
        List<UserAuthDTO> collect = page.getRecords().stream()
                .map(userConverter::toAuthDTO)
                .collect(Collectors.toList());
        return new PageDTO<>(collect, page);
    }
}
