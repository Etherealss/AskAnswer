package cn.hwb;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

/**
 * @author hwb
 */
@Slf4j
public class EncrytorTest {
    private static final String ALGORITHM_INFO = "PBEWITHHMACSHA512ANDAES_256";
    private static final String PASSWORD_INFO = "";
    private static final String[] PASSWORDS = {
    };

    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(ALGORITHM_INFO);
        encryptor.setPassword(PASSWORD_INFO);
        encryptor.setIvGenerator(new RandomIvGenerator());
        //要加密的文本
        for (String password : PASSWORDS) {
            String encrypt = encryptor.encrypt(password);
            log.info("{} 密文：{}", password, encrypt);
        }
    }

}
