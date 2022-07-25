package com.plusl.core.facade;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LJH
 */
@Slf4j
@EnableDubbo
@EnableApolloConfig
@EnableConfigurationProperties
@MapperScan("com.plusl.core.service.mapper")
@ComponentScan(basePackages = {"com.plusl.core" , "com.plusl.framework"})
@SpringBootApplication
public class App {

    public static void main(String[] args) {

        SpringApplication.run(App.class, args);
    }

}
