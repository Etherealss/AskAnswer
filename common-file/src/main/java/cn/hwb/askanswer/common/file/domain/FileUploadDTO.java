package cn.hwb.askanswer.common.file.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadDTO {
    String fileDir;
    String fileName;
    String fileExt;
    String fileContentType;
    String fileKey;
    String url;
}
