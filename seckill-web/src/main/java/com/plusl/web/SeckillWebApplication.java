package com.plusl.web;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.plusl.web.config.DubboConsumerConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collections;

/**
 * @author LJH
 */
@EnableDubbo
@EnableApolloConfig
@MapperScan("com.plusl.core.service.mapper")
@SpringBootApplication(scanBasePackages = {"com.plusl.web"})
public class SeckillWebApplication {

    private static final String RES_KEY = "com.plusl.core.service.SeckillService:createOrderAndReduceStock(User, GoodsDTO)";
    private static final String INTERFACE_RES_KEY = "com.plusl.core.service.SeckillService";

    public static void main(String[] args) {
        // 初始化限流规则
        initFolwRule();

        AnnotationConfigApplicationContext consumerContext = new AnnotationConfigApplicationContext();
        consumerContext.register(DubboConsumerConfig.class);
        consumerContext.refresh();

        SpringApplication.run(SeckillWebApplication.class, args);
    }

    private static void initFolwRule() {
        FlowRule flowRule = new FlowRule();
        flowRule.setResource(RES_KEY);
        flowRule.setCount(5);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setLimitApp("default");
        FlowRuleManager.loadRules(Collections.singletonList(flowRule));
    }
}
