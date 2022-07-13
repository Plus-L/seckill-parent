package com.plusl.service.mapper;

import com.plusl.common.entity.User;
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

    public User getByNickname(@Param("nickname") String nickname);

    public User getById(@Param("id") long id);

    public void update(User toBeUpdate);

    public void insertMiaoShaUser(User miaoshaUser);

    public int getCountByUserName(@Param("userName") String userName, @Param("userType") int userType);


}
