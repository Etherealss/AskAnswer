package cn.hwb.askanswer.common.database.repository;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author hwb
 */
@EnableTransactionManagement
@Configuration
public class MyBatisPlusConfig {
    /**
     * MP 分页插件
     * @return
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
        interceptor.setDbType(DbType.MYSQL);
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        interceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        interceptor.setMaxLimit(500L);
        return interceptor;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }
}

