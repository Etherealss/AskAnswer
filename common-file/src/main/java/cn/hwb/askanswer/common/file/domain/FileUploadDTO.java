package cn.hwb.askanswer.common.file.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author wtk
 * @date 2022-09-04
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadDTO {
    String filePath;
    String fileName;
    String fileExt;
    String fileContentType;
    String fileKey;
    String url;
}
