package cn.hwb.askanswer.common.base.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * {@code @PropertySource} 默认只能读取properties配置文件
 * 当前类实现yaml配置文件的读取（同时兼容properties）
 * 用法：
 * {@code @PropertySource(value = "classpath:config/xxx.yaml", factory = CompositePropertySourceFactory.class)}
 * 见：https://www.jianshu.com/p/2e66eb00d466
 * @author wtk
 * @date 2022-02-12
 */
@Component
public class CompositePropertySourceFactory extends DefaultPropertySourceFactory {

    /**
     * 文件结尾
     */
    public static final String YML = ".yml",
                               YAML = ".yaml";

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource)
            throws IOException {
        String sourceName = Optional.ofNullable(name)
                .orElse(resource.getResource().getFilename());
        if (!StringUtils.hasText(sourceName)) {
            throw new IOException("读取配置文件时，获取不到源文件");
        }
        if (!resource.getResource().exists()) {
            // return an empty Properties
            return new PropertiesPropertySource(sourceName, new Properties());
        } else if (sourceName.endsWith(YML) || sourceName.endsWith(YAML)) {
            Properties propertiesFromYaml = loadYaml(resource);
            return new PropertiesPropertySource(sourceName, propertiesFromYaml);
        } else {
            return super.createPropertySource(name, resource);
        }
    }

    /**
     * load yaml file to properties
     * @param resource
     * @return
     * @throws IOException
     */
    private Properties loadYaml(EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}