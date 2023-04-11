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
 * @author hwb
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

    private String getSaveDir() {
        return userAvatarProperties.getDir() + "/" + DATE_FORMAT.format(new Date());
    }

    /**
     * 上传审核图片
     * @param username
     * @param imgFile
     * @return 头像保存路径，同时也是访问文件的url
     */
    public FileUploadDTO uploadReview(String username, MultipartFile imgFile) {
        ImgFileUtil.verifyImgFile(imgFile);
        String originalFilename = imgFile.getOriginalFilename();
        assert originalFilename != null;
        String fileExt = FileUtil.getFileExt(originalFilename);
        String fileName = username + fileExt;
        try {
            return fileService.uploadFile(imgFile, getSaveDir(), fileName);
        } catch (IOException e) {
            log.error("用户头像上传失败：username：{}，fileName：{}", username, fileName, e);
            throw new RuntimeException(e);
        }
    }

}
