package com.plusl.core.facade;

import com.alibaba.csp.sentinel.init.InitExecutor;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.plusl.core.service.config.DubboProviderConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LJH
 */
@Slf4j
@EnableDubbo
@EnableApolloConfig
@EnableConfigurationProperties
@MapperScan("com.plusl.core.service.mapper")
@ComponentScan(basePackages = {"com.plusl.core", "com.plusl.framework"})
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        // 加载注册的init函数并按顺序执行-Sentinel初始化。
        InitExecutor.doInit();
        // AnnotationConfigApplicationContext 独立的应用程序上下文，接受组件类作为输入——特别是 @Configuration 注释的类，
        // 还有普通的 @Component 类型和使用 javax.inject 注释的 JSR-330 兼容类。
        // 允许使用 register(Class...) 逐个注册类，以及使用 scan(String...) 进行类路径扫描
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DubboProviderConfig.class);
//        context.refresh();
        SpringApplication.run(App.class, args);
    }

}
