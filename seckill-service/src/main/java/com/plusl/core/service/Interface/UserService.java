package com.plusl.core.service.Interface;

import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: seckill-parent
 * @description: 用户服务接口
 * @author: PlusL
 * @create: 2022-07-11 11:14
 **/
public interface UserService {

    boolean login(HttpServletResponse response, LoginVo loginVo);

    String createToken(HttpServletResponse response, LoginVo loginVo);

    User getByToken(HttpServletResponse response, String token);

    User getByNickName(String nickName);
}
