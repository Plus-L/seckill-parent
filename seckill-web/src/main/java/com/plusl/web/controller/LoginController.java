package com.plusl.web.controller;

import com.plusl.framework.common.dto.UserLoginDTO;
import com.plusl.framework.common.dto.UserWithTokenDTO;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.web.vo.LoginVo;
import com.plusl.web.client.UserClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.plusl.framework.redis.constant.RedisConstant.USER_EXPIRE_TIME;


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
    private static final String COOKIE_NAME_TOKEN = "TOKEN";

    @Autowired
    private UserClient userClient;


    @RequestMapping("/to_login")
    public String tologin(LoginVo loginVo, Model model) {
        LOGGER.info(loginVo.toString());
        return "login";
    }

    @PostMapping("/do_login")
    @ResponseBody
    public CommonResult<UserWithTokenDTO> dologin(HttpServletResponse response, @Valid UserLoginDTO userLoginDTO) {
        UserWithTokenDTO userWithTokenDTO = userClient.checkPasswordAndLogin(userLoginDTO);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, userWithTokenDTO.getToken());
        //设置有效期
        cookie.setMaxAge(USER_EXPIRE_TIME.intValue());
        cookie.setPath("/");
        response.addCookie(cookie);
        return CommonResult.success(userWithTokenDTO);
    }


    @GetMapping("/create_token")
    @ResponseBody
    public String createToken(HttpServletResponse response, @Valid UserLoginDTO userLoginDTO) {
        LOGGER.info(userLoginDTO.toString());
        String token = userClient.createToken(userLoginDTO);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置有效期
        cookie.setMaxAge(USER_EXPIRE_TIME.intValue());
        cookie.setPath("/");
        response.addCookie(cookie);
        return token;
    }

}
