package com.plusl.core;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.plusl.core.service.config.DubboProviderConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Collections;

/**
 * @program: seckill-parent
 * @description: 服务层启动类
 * @author: PlusL
 * @create: 2022-07-13 10:36
 **/
@EnableAutoConfiguration
@MapperScan("com.plusl.core.service.mapper")
public class ServiceBootstrapApp {

    public static void main(String[] args) throws IOException {
        //初始化限流规则
        initFlowRule();
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(DubboProviderConfig.class);
        ((AnnotationConfigApplicationContext) applicationContext).start();
        System.in.read();
    }

    private static void initFlowRule() {
        FlowRule flowRule = new FlowRule();
        //针对具体的方法限流
        flowRule.setResource("com.plusl.service.SeckillService:doSeckill(com.plusl.framework.common.entity.OrderInfo)");
        //限流阈值 qps=10
        flowRule.setCount(10);
        //限流阈值类型（QPS 或并发线程数）
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //流控针对的调用来源，若为 default 则不区分调用来源
        flowRule.setLimitApp("default");
        // 流量控制手段（直接拒绝、Warm Up、匀速排队）
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        FlowRuleManager.loadRules(Collections.singletonList(flowRule));
    }
}
