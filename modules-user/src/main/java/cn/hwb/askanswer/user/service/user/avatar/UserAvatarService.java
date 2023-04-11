package cn.hwb.askanswer.user.service.user.avatar;

import cn.hwb.askanswer.common.base.utils.FileUtil;
import cn.hwb.askanswer.common.file.domain.FileService;
import cn.hwb.askanswer.common.file.domain.FileUploadDTO;
import cn.hwb.askanswer.common.file.infrastructure.utils.ImgFileUtil;
import cn.hwb.askanswer.user.infrastructure.config.UserAvatarProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hwb
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

    /**
     * 按日期分文件夹
     * @return
     */
    private String getSaveDir() {
        return userAvatarProperties.getDir() + "/" + DATE_FORMAT.format(new Date());
    }

    /**
     * 获取默认头像文件的URL
     * @return
     */
    @Cacheable(value = {"AskAnswer:cache:avatar:str"}, key = "'default-avatars'")
    public String getDefaultAvatar() {
        return fileService.getFileUrl(userAvatarProperties.getDefaultAvatarKey());
    }


    /**
     * 上传头像
     * @param avatarFile 头像文件流
     * @param userId
     * @return 访问头像图片的url
     */
    public FileUploadDTO uploadAvatar(MultipartFile avatarFile, Long userId) {
        ImgFileUtil.verifyImgFile(avatarFile);
        String originalFilename = avatarFile.getOriginalFilename();
        assert originalFilename != null;
        String fileExt = FileUtil.getFileExt(originalFilename);
        String fileName = userId + fileExt;
        try {
            return fileService.uploadFile(avatarFile, getSaveDir(), fileName);
        } catch (IOException e) {
            log.error("用户头像上传失败：userId：{}，fileName：{}", userId, fileName, e);
            throw new RuntimeException(e);
        }
    }
}
