package cn.hwb.common.security.agelimit;

/**
 * @author wtk
 * @date 2023-04-05
 */
public interface AgeLimitVerifier {
    boolean verify(Long id);
}
