package cn.hwb.askanswer.user.service.user;

import cn.hwb.askanswer.common.base.crypt.PasswordEncryptor;
import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
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
import cn.hwb.askanswer.user.infrastructure.pojo.request.UpdateUserInfoRequest;
import cn.hwb.askanswer.user.mapper.UserMapper;
import cn.hwb.askanswer.user.service.user.avatar.UserAvatarService;
import cn.hwb.askanswer.user.service.user.review.UserReviewService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 注册用户
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(CreateUserRequest request) {
        // 用户名是否重复
        if (this.checkUsernameExists(request.getUsername())) {
            throw new ExistException(UserEntity.class, request.getUsername());
        }
        UserEntity userEntity = userConverter.toEntity(request)
                .setAvatar(userAvatarService.getDefaultAvatar())
                .setRoles(new ArrayList<>(0))
                .setIsReviewed(false)
                .setReviewImg("");
        // 密码加密
        userEntity.setPassword(passwordEncryptor.encode(userEntity.getPassword()));
        this.save(userEntity);
        return userEntity.getId();
    }

    /**
     * 获取用户基本信息
     * @param userId
     * @return
     */
    public UserBriefDTO getBriefById(Long userId) {
        UserEntity user = lambdaQuery()
                .eq(UserEntity::getId, userId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(UserEntity.class, userId.toString()));
        UserBriefDTO dto = userConverter.toBriefDTO(user);
        dto.setAgeBracket(AgeBracketEnum.getByBirthday(dto.getBirthday()).getCode());
        return dto;
    }

    /**
     * 批量获取用户信息
     * @param userIds
     * @return
     */
    public List<UserBriefDTO> getBatchBriefsByIds(Collection<Long> userIds) {
        List<UserEntity> users = userMapper.selectBatchIds(userIds);
        return users.stream()
                .map(entity -> {
                    UserBriefDTO dto = userConverter.toBriefDTO(entity);
                    dto.setAgeBracket(AgeBracketEnum.getByBirthday(dto.getBirthday()).getCode());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    /**
     * 用户名是否重复
     * @param username
     * @return
     */
    public boolean checkUsernameExists(String username) {
        return lambdaQuery().eq(UserEntity::getUsername, username).count() > 0;
    }

    /**
     * 更新用户信息
     * @param userId
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Long userId, UpdateUserInfoRequest req) {
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
            update.setPassword(passwordEncryptor.encode(newPassword));
        }
        update.setId(userId);
        this.saveOrUpdate(update);
    }

    /**
     * 更新用户头像
     * @param userId
     * @param file
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateAvatar(Long userId, MultipartFile file) {
        // 上传新的头像文件
        FileUploadDTO fileUploadDTO = userAvatarService.uploadAvatar(file, userId);
        // 保存新头像的url
        String url = fileUploadDTO.getUrl();
        boolean update = this.lambdaUpdate()
                .eq(UserEntity::getId, userId)
                .set(UserEntity::getAvatar, url)
                .update();
        if (!update) {
            // 有Token校验在前，此处一般不会NotFound，但为了健壮性还是要检查一下
            throw new NotFoundException(UserEntity.class, userId.toString());
        }
        return url;
    }

    /**
     * 更新用户审核图片
     * @param userId
     * @param file
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateReviewImg(Long userId, MultipartFile file) {
        // 上传审核图片
        FileUploadDTO fileUploadDTO = userReviewService.uploadAvatar(userId, file);
        // 更新审核图片的url
        String reviewImgUrl = fileUploadDTO.getUrl();
        boolean update = this.lambdaUpdate()
                .eq(UserEntity::getId, userId)
                .set(UserEntity::getReviewImg, reviewImgUrl)
                .update();
        if (!update) {
            throw new NotFoundException(UserEntity.class, userId.toString());
        }
    }

    /**
     * 审核通过，更新用户审核状态
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void reviewPass(Long userId) {
        boolean update = this.lambdaUpdate()
                .eq(UserEntity::getId, userId)
                .set(UserEntity::getIsReviewed, true)
                .update();
        if (!update) {
            throw new NotFoundException(UserEntity.class, userId.toString());
        }
    }

    /**
     * 审核不通过，更新用户审核状态
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void reviewFail(Long userId) {
        userMapper.deleteById(userId);
    }

    /**
     * 获取用户审核图片url
     * @param userId
     */
    public String getReviewImg(Long userId) {
        UserEntity entity = this.lambdaQuery()
                .eq(UserEntity::getId, userId)
                .select(UserEntity::getReviewImg)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(UserEntity.class, userId.toString()));
        return entity.getReviewImg();
    }

    /**
     * 获取用户信息，用于审核
     * @param userId
     */
    public UserAuthDTO get4Review(Long userId) {
        UserEntity entity = this.lambdaQuery()
                .eq(UserEntity::getId, userId)
                .oneOpt()
                .orElseThrow(() -> new NotFoundException(UserEntity.class, userId.toString()));
        return userConverter.toAuthDTO(entity);
    }

    /**
     * 分页获取用户信息，用于审核
     * @param currentPage
     * @param size
     */
    public PageDTO<UserAuthDTO> page4Review(int currentPage, int size) {
        Page<UserEntity> pageSpec = new Page<>(currentPage, size);
        Page<UserEntity> page = lambdaQuery()
                .eq(UserEntity::getIsReviewed, false)
                .page(pageSpec);
        List<UserAuthDTO> collect = page.getRecords().stream()
                .map(userConverter::toAuthDTO)
                .collect(Collectors.toList());
        return new PageDTO<>(collect, page);
    }
}
