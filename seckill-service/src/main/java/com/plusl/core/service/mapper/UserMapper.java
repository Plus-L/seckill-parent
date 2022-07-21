package com.plusl.core.service.mapper;

import com.plusl.framework.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @program: seckill-parent
 * @description: 用户数据库Mapper
 * @author: PlusL
 * @create: 2022-07-06 09:49
 **/
@Mapper
public interface UserMapper {

    User getByNickname(@Param("nickname") String nickname);

    User getById(@Param("id") long id);

    void update(User toBeUpdate);

    void insertSeckillUser(User user);

    int getCountByUserName(@Param("userName") String userName, @Param("userType") int userType);


}
