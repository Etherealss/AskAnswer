package cn.hwb.common.file.infrastructure.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.BucketInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author wang tengkun
 * @date 2023/4/4
 */
@Configuration
@ConfigurationProperties("app.oss")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
@Slf4j
public class OssProperties {
    /**
     * 文件客户端类型
     */
    private String fileClientType;
    /**
     * OSS地址（不同服务器，地址不同）
     */
    private String endpoint;
    /**
     * OSS键id（去OSS控制台获取）
     */
    private String accessKeyId;
    /**
     * OSS秘钥（去OSS控制台获取）
     */
    private String accessKeySecret;
    /**
     * OSS桶名称（这个是自己创建bucket时候的命名）
     */
    private String bucketName;

    @Bean
    public OSS oss() {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
        if (ossClient.doesBucketExist(bucketName)) {
            log.info("OSS Bucket {} 已存在", bucketName);
        } else {
            log.error("OSS Bucket {} 不存在，请检查配置", bucketName);
            throw new RuntimeException("OSS Bucket不存在");
        }

        // 查看Bucket信息。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
        BucketInfo info = ossClient.getBucketInfo(bucketName);
        log.info("Bucket {} 的信息如下：\n数据中心：{}\n创建时间：{}\n用户标志：{}",
                bucketName,
                info.getBucket().getLocation(),
                info.getBucket().getCreationDate(),
                info.getBucket().getOwner()
        );
        return ossClient;
    }
}
