package com.plusl.web.controller;


import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: seckill-parent
 * @description: test
 * @author: PlusL
 * @create: 2022-07-06 15:33
 **/
@RestController
@RequestMapping("/demo")
public class TestController {

//    @DubboReference
//    private MqProducer mqProducer;

//    @DubboReference
//    private UserService userService;
//
//    @DubboReference
//    private DubboTestFacade dubboTestFacade;

    @RequestMapping("/mqtest")
    public void testRocketMq() {
//        mqProducer.sendSeckillMessage("123456");
    }

    @PostMapping(value = "/sysHello")
    public void sysHello(String name) {
//        System.out.println(dubboTestFacade.sysHello(name));
    }

    public static void main(String[] args) {
        Config config = ConfigService.getAppConfig();
        String value = config.getProperty("spring.datasource.driver-class-name", "获取失败");
        System.out.println(value);
    }
}
