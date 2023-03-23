package cn.hwb.common.security.token;


import cn.hwb.common.security.auth.exception.TokenException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author wtk
 * @date 2022-08-30
 */
public interface ITokenService<T extends TokenCertificate> {

    void completeTokenAndSave(@NonNull TokenCertificate credential);

    // TODO updateUserTokenCertificate 在用户权限更新时修改token权限缓存

    @NonNull
    T assertToken(String token) throws TokenException;

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
    T getToken(String token);
}
