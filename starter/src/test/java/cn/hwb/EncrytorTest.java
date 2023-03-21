package cn.hwb;

import com.ulisesbocchio.jasyptspringboot.encryptor.ByteEncryptorStringEncryptorDelegate;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wtk
 * @date 2023-03-21
 */
@Slf4j
public class EncrytorTest {
    private static final String ALGORITHM_INFO = "PBEWithMD5AndDES";
    private static final String PASSWORD_INFO = "wenmumu";
    private static final String[] PASSWORDS = {
            "123456",
            "baotaredis123456",
            "baotamysql123456"
    };

    public static void main(String[] args) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        //配置文件中配置如下的算法
        standardPBEStringEncryptor.setAlgorithm(ALGORITHM_INFO);
        //配置文件中配置的password
        standardPBEStringEncryptor.setPassword(PASSWORD_INFO);
        //要加密的文本
        for (String password : PASSWORDS) {
            String encrypt = standardPBEStringEncryptor.encrypt("root");
            log.info("{} 密文：{}", password, encrypt);
        }
    }

}
