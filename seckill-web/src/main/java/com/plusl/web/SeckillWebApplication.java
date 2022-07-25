package com.plusl.web;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LJH
 */
@EnableDubbo
@EnableApolloConfig
@MapperScan("com.plusl.core.service.mapper")
@SpringBootApplication(scanBasePackages = {"com.plusl.web"})
public class SeckillWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillWebApplication.class, args);
    }

}
