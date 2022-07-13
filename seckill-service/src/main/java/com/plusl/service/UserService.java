package com.plusl.service;

import com.plusl.common.entity.User;
import com.plusl.common.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: seckill-parent
 * @description: 用户服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:14
 **/
public interface UserService {

    public boolean login(HttpServletResponse response, LoginVo loginVo);

    public String createToken(HttpServletResponse response, LoginVo loginVo);

    public User getByToken(HttpServletResponse response, String token);

    public User getByNickName(String nickName);
}
