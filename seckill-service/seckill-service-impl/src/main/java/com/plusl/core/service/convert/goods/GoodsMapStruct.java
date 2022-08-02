package com.plusl.core.service.convert.goods;


import com.plusl.core.service.dataobject.GoodsDO;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.SeckillGoods;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: seckill-parent
 * @description: 商品对象转换器
 * @author: PlusL
 * @create: 2022-07-18 17:13
 **/
@Mapper
public interface GoodsMapStruct {

    GoodsMapStruct INSTANCE = Mappers.getMapper(GoodsMapStruct.class);

    /**
     * 商品DO->DTO
     *
     * @param goodsDO 商品数据对象
     * @return 商品数据传输对象
     */
    GoodsDTO convert(GoodsDO goodsDO);



    /**
     * GoodsDO -> SeckillGoods
     *
     * @param goodsDTO 商品DO
     * @return 秒杀商品
     */
    @Mapping(source = "goodsId", target = "goodsId")
    SeckillGoods toSeckillGoods(GoodsDTO goodsDTO);

    /**
     * 商品DO列表->DTO列表
     *
     * @param goodsDOList DO列表
     * @return DTO列表
     */
    List<GoodsDTO> convertListDOtoDTO(List<GoodsDO> goodsDOList);




}
