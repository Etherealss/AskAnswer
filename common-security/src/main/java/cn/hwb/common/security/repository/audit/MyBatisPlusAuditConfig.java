package cn.hwb.common.security.repository.audit;

import com.ulisesbocchio.jasyptspringboot.annotation.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wtk
 * @date 2023-03-22
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
