package cn.hwb.askanswer.user.service.user.review;

import cn.hwb.askanswer.common.base.utils.FileUtil;
import cn.hwb.askanswer.common.file.domain.FileService;
import cn.hwb.askanswer.common.file.domain.FileUploadDTO;
import cn.hwb.askanswer.common.file.infrastructure.utils.ImgFileUtil;
import cn.hwb.askanswer.user.infrastructure.config.UserReviewProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wtk
 * @date 2023-04-04
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserReviewService {
    /**
     * 用于在OSS上命名，格式 ：年月日/文件名.后缀名
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final UserReviewProperties userAvatarProperties;
    private final FileService fileService;

    /**
     * 上传审核图片
     * @param imgFile
     * @param userId
     * @return 头像保存路径，同时也是访问文件的url
     */
    public FileUploadDTO uploadAvatar(MultipartFile imgFile, Long userId) {
        ImgFileUtil.verifyImgFile(imgFile);
        String originalFilename = imgFile.getOriginalFilename();
        assert originalFilename != null;
        String fileExt = FileUtil.getFileExt(originalFilename);
        String fileName = userId + fileExt;
        try {
            return fileService.uploadFile(imgFile, getSaveDir(), fileName);
        } catch (IOException e) {
            log.error("用户头像上传失败：userId：{}，fileName：{}", userId, fileName, e);
            throw new RuntimeException(e);
        }
    }

    private String getSaveDir() {
        return userAvatarProperties.getDir() + "/" + DATE_FORMAT.format(new Date());
    }
}
