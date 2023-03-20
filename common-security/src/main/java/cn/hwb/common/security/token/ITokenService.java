package cn.hwb.common.security.token;


/**
 * @author wtk
 * @date 2022-08-30
 */
public interface ITokenService<T extends TokenCertificate> {

    void createToken(TokenCertificate credential);

    // TODO updateUserTokenCertificate 在用户权限更新时修改token权限缓存

    T verifyToken(String token);

    /**
     * 使 token 失效
     * @param token
     * @param <T>
     */
    void invalidateToken(String token);
}
