package cn.hwb.askanswer;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hwb
 */
// 指定要扫描的Mapper类的包的路径，需要加上Mapper注解，否则会把访问到的接口都当成Mapper类
@MapperScan(value = "cn.hwb", annotationClass = Mapper.class)
@ComponentScan("cn.hwb") // 扫描其他子模块的组件
@SpringBootApplication()
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}