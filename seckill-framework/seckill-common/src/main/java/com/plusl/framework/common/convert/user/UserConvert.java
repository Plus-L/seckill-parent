package com.plusl.framework.common.convert.user;

import com.plusl.framework.common.dto.UserLoginDTO;
import com.plusl.framework.common.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @program: seckill-parent
 * @description: 用户对象转换器-通过MapStruct转换对象（DO->DTO->entity->VO）
 * @author: PlusL
 * @create: 2022-07-18 17:10
 **/
@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * dto -> do
     * @param userLoginDTO dto
     * @return do
     */
    User convert(UserLoginDTO userLoginDTO);

    /**
     * entity -> dto
     * @param user entity
     * @return dto
     */
    UserLoginDTO toDTO(User user);



}
