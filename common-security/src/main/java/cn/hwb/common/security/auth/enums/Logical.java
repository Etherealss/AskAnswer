package cn.hwb.common.security.auth.enums;

/**
 * 权限校验时采用的模式
 * @author wtk
 */
public enum Logical {
    /**
     * 必须具有所有权限
     */
    AND,

    /**
     * 只需具有其中一个权限
     */
    OR
}
