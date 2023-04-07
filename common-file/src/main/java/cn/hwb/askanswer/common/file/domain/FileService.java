package cn.hwb.askanswer.common.file.domain;

import cn.hwb.askanswer.common.base.exception.rest.ParamMissingException;
import cn.hwb.askanswer.common.base.utils.FileUtil;
import cn.hwb.askanswer.common.file.infrastructure.service.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author hwb
 */
@Service
@Slf4j
@Validated
public class FileService {

    private final OssFileService ossFileService;

    public FileService(OssFileService ossFileService) {
        this.ossFileService = ossFileService;
    }

    /**
     * 上传文件
     * @param avatarFile HTTP 文件流
     * @param fileDir 要保存的文件夹
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public FileUploadDTO uploadFile(MultipartFile avatarFile, String fileDir, String fileName) throws IOException {
        if (!StringUtils.hasText(fileName)) {
            fileName = avatarFile.getOriginalFilename();
        }
        if (!StringUtils.hasText(fileName)) {
            throw new ParamMissingException("文件名不能为空");
        }
        String fileExt = FileUtil.getFileExt(fileName);
        String fileContentType = FileUtil.getContentType(fileExt);
        String fileKey = fileDir + "/" + fileName;
        String fileUrl = ossFileService.upload(avatarFile.getInputStream(), fileKey, fileContentType);

        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setFileKey(fileKey);
        fileUploadDTO.setFileDir(fileDir);
        fileUploadDTO.setFileName(fileName);
        fileUploadDTO.setFileContentType(fileContentType);
        fileUploadDTO.setFileExt(fileExt);
        fileUploadDTO.setUrl(fileUrl);
        log.info("文件上传成功：{}", fileUploadDTO);
        return fileUploadDTO;
    }

    /**
     * 获取OSS的文件访问URL
     * @param fileKey 文件在OSS上的存储位置
     * @return 文件访问URL
     */
    public String getFileUrl(String fileKey) {
        return ossFileService.getUrl(fileKey);
    }

}
