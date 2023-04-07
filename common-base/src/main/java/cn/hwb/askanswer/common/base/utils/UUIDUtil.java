package cn.hwb.askanswer.common.base.utils;

import java.util.UUID;

/**
 * @author hwb
 */
public class UUIDUtil {
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
