package cn.hwb.common.security.token;


import cn.hwb.common.security.token.user.UserTokenCertificate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author wtk
 * @date 2022-08-30
 */
public interface ITokenService<T extends TokenCertificate> {

    void createTokenAndSave(@NonNull TokenCertificate credential);

    // TODO updateUserTokenCertificate 在用户权限更新时修改token权限缓存

    @NonNull
    T assertToken(String token);

    /**
     * 使 token 失效
     * @param token
     */
    void invalidateToken(String token);

    /**
     * 获取token
     * @param token
     * @return 不存在时返回null
     */
    @Nullable
    UserTokenCertificate getToken(String token);
}
