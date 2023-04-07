package cn.hwb.askanswer.common.database.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hwb
 */
@Configuration
public class MyBatisPlusAuditConfig {
    /**
     * 审计数据插件
     *
     * @return AuditMetaObjectHandler
     */
    @Bean
    public AuditMetaObjectHandler auditMetaObjectHandler() {
        return new AuditMetaObjectHandler();
    }
}
