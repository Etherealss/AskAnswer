package cn.hwb.askanswer.common.base.crypt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Component
@Slf4j
public class PasswordEncryptor {
    public static final String ALGORITHM = "SHA-256";
    private final MessageDigest messageDigest;

    {
        try {
            messageDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("摘要算法不存在", e);
        }
    }

    /**
     * 加盐哈希算法
     * @param password 明文密码
     * @return 加密后的密码
     * @throws NoSuchAlgorithmException
     */
    public String encode(String password) {
        // 进行SHA-256哈希
        byte[] encrypted = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        BigInteger bigInt = new BigInteger(1, encrypted);
        String encode = bigInt.toString(16);
        log.debug("原始密码：{}，加密密文：{}", password, encode);
        return encode;
    }

    /**
     * 验证用户密码是否正确
     * @param checkPassword 原始密码
     * @param realPassword 密文
     * @return true：密码正确；false：密码错误
     * @throws NoSuchAlgorithmException
     */
    public boolean match(String checkPassword, String realPassword) {
        String encodedPassword = this.encode(checkPassword);
        return encodedPassword.equals(realPassword);
    }
}
