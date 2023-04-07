package cn.hwb.common.security.agelimit;

/**
 * 年龄段限制的校验接口
 * @author wtk
 * @date 2023-04-05
 */
public interface AgeLimitVerifier {
    boolean verify(Long id);
}
