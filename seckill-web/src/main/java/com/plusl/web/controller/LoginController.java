package com.plusl.web.controller;

import com.plusl.common.enums.result.Result;
import com.plusl.common.vo.LoginVo;
import com.plusl.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @program: seckill-parent
 * @description: 登录控制器
 * @author: PlusL
 * @create: 2022-07-05 17:24
 **/
@Controller
@RequestMapping("/login")
public class LoginController {

    public static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @Autowired
    private UserService userService;


    @RequestMapping("/to_login")
    public String tologin(LoginVo loginVo, Model model) {
        LOGGER.info(loginVo.toString());
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> dologin(HttpServletResponse response, @Valid LoginVo loginVo) {
        Result<String> result = Result.build();
        LOGGER.info(loginVo.toString());
        userService.login(response, loginVo);
        return result;
    }


    @RequestMapping("/create_token")
    @ResponseBody
    public String createToken(HttpServletResponse response, @Valid LoginVo loginVo) {
        LOGGER.info(loginVo.toString());
        String token = userService.createToken(response, loginVo);
        return token;
    }

}
