package cn.hwb.askanswer.common.base.utils;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wtk
 * @date 2022-04-30
 */
public class FileUtil {
    public static final Map<String, String> FILE_EXTENSIONS;

    static {
        FILE_EXTENSIONS = new HashMap<>();
        FILE_EXTENSIONS.put(".bmp", "image/bmp");
        FILE_EXTENSIONS.put(".gif", "image/gif");
        FILE_EXTENSIONS.put(".jpeg", "image/jpg");
        FILE_EXTENSIONS.put(".jpg", "image/jpg");
        FILE_EXTENSIONS.put(".png", "image/png");
        FILE_EXTENSIONS.put(".html", "text/html");
        FILE_EXTENSIONS.put(".txt", "text/plain");
        FILE_EXTENSIONS.put(".vsd", "application/vnd.visio");
        FILE_EXTENSIONS.put(".pptx", "application/vnd.ms-powerpoint");
        FILE_EXTENSIONS.put(".ppt", "application/vnd.ms-powerpoint");
        FILE_EXTENSIONS.put(".docx", "application/msword");
        FILE_EXTENSIONS.put(".doc", "application/msword");
        FILE_EXTENSIONS.put(".xml", "text/xml");
    }

    /**
     * 解决问题，直接访问上传的图片地址，会让下载而不是直接访问
     * 设置设置 HTTP 头 里边的 Content-Type
     * txt 格式经过测试，不需要转换 上传之后就是 text/plain。其他未测试
     * 已知  如果 Content-Type = .jpeg 访问地址会直接下载，本方法也是解决此问题
     * @param filenameExtension
     * @return
     */
    public static String getContentType(String filenameExtension) {
        if (StrUtil.isBlank(filenameExtension)) {
            throw new IllegalArgumentException("文件扩展名不能为空");
        }
        String contentType = FILE_EXTENSIONS.get(filenameExtension.toLowerCase());
        if (contentType == null) {
            throw new IllegalArgumentException("不支持的文件类型：" + filenameExtension);
        }
        return contentType;
    }

    /**
     * 获取文件扩展名
     * @return
     */
    public static String getFileExt(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        String result = fileName.substring(index);
        return result;
    }
}
