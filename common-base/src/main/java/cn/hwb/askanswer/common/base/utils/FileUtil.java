package cn.hwb.askanswer.common.base.utils;

import cn.hutool.core.util.StrUtil;

import java.util.*;

/**
 * @author wtk
 * @date 2022-04-30
 */
public class FileUtil {
    private static final Set<String> IMAGE_EXT;
    public static final Map<String, String> FILE_EXTENSIONS;

    static {
        Map<String, String> map = new HashMap<>();
        map.put(".bmp", "image/bmp");
        map.put(".gif", "image/gif");
        map.put(".jpeg", "image/jpg");
        map.put(".jpg", "image/jpg");
        map.put(".png", "image/png");
        map.put(".html", "text/html");
        map.put(".txt", "text/plain");
        map.put(".vsd", "application/vnd.visio");
        map.put(".pptx", "application/vnd.ms-powerpoint");
        map.put(".ppt", "application/vnd.ms-powerpoint");
        map.put(".docx", "application/msword");
        map.put(".doc", "application/msword");
        map.put(".xml", "text/xml");
        FILE_EXTENSIONS = Collections.unmodifiableMap(map);

        Set<String> set = new HashSet<>();
        set.add(".jpeg");
        set.add(".jpg");
        set.add(".png");
        IMAGE_EXT = Collections.unmodifiableSet(set);
    }

    public static boolean isImageExt(String fileExt) {
        return IMAGE_EXT.contains(fileExt);
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
