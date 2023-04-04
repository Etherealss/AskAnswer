package cn.hwb.askanswer.user.service.user.avatar;

import cn.hwb.askanswer.common.base.exception.rest.ParamErrorException;
import cn.hwb.askanswer.common.base.exception.rest.ParamMissingException;
import cn.hwb.askanswer.common.base.utils.FileUtil;
import cn.hwb.askanswer.common.file.domain.FileService;
import cn.hwb.askanswer.common.file.domain.FileUploadDTO;
import cn.hwb.askanswer.user.infrastructure.config.UserAvatarProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAvatarService {
    /**
     * 用于在OSS上命名，格式 ：年月日/文件名.后缀名
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final UserAvatarProperties userAvatarProperties;
    private final FileService fileService;


    @Cacheable("${app.user.avatar.oss.cache-key}")
    public String getDefaultAvatar() {
        return fileService.getFileUrl(userAvatarProperties.getDefaultAvatarKey());
    }


    /**
     * 上传头像
     * @param avatarFile
     * @param userId
     * @return 头像保存路径，同时也是访问文件的url
     */
    public FileUploadDTO uploadAvatar(MultipartFile avatarFile, Long userId) {
        if (avatarFile.isEmpty()) {
            throw new ParamMissingException("文件流为空");
        }
        String originalFilename = avatarFile.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new ParamMissingException("无法获取原始文件的文件扩展名");
        }
        String fileExt = FileUtil.getFileExt(originalFilename);
        if (!FileUtil.isImageExt(fileExt)) {
            throw new ParamErrorException("非图片文件");
        }
        String fileName = userId + fileExt;
        try {
            return fileService.uploadFile(avatarFile, getSaveDir(), fileName);
        } catch (IOException e) {
            log.error("用户头像上传失败：userId：{}，fileName：{}", userId, fileName, e);
            throw new RuntimeException(e);
        }
    }

    private String getSaveDir() {
        return userAvatarProperties.getDir() + "/" + DATE_FORMAT.format(new Date());
    }
}
