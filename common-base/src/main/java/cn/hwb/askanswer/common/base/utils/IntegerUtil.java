package cn.hwb.askanswer.common.base.utils;

/**
 * @author wtk
 * @date 2023-03-25
 */
public class IntegerUtil {
    public static int long2Int(long num) {
        return num < Integer.MAX_VALUE ? (int) num : Integer.MAX_VALUE;
    }
}
