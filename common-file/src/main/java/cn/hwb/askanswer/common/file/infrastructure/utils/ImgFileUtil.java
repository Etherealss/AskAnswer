package cn.hwb.askanswer.common.file.infrastructure.utils;

import cn.hwb.askanswer.common.base.exception.rest.ParamErrorException;
import cn.hwb.askanswer.common.base.exception.rest.ParamMissingException;
import cn.hwb.askanswer.common.base.utils.FileUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hwb
 */
public class ImgFileUtil {

    public static void verifyImgFile(MultipartFile file) throws ParamMissingException, ParamErrorException {
        if (file.isEmpty()) {
            throw new ParamMissingException("文件流为空");
        }
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new ParamMissingException("无法获取原始文件的文件扩展名");
        }
        String fileExt = FileUtil.getFileExt(originalFilename);
        if (!FileUtil.isImageExt(fileExt)) {
            throw new ParamErrorException("非图片文件");
        }
    }
}
