package com.plusl.framework.common.convert.user;

import com.plusl.framework.common.dataobject.GoodsDO;
import com.plusl.framework.common.dto.GoodsDTO;
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
     * DO -> DTO
     * @param goodsDO 商品数据对象
     * @return 商品数据传输对象
     */
    GoodsDTO convert(GoodsDO goodsDO);



}
