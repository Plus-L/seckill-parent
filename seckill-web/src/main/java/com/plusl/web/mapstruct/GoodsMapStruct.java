package com.plusl.web.mapstruct;

import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.core.facade.api.entity.SeckillGoods;
import com.plusl.web.vo.GoodsVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: seckill-facade-api
 * @description: Web层商品转换类MapStruct
 * @author: PlusL
 * @create: 2022-08-01 09:22
 **/
@Mapper
public interface GoodsMapStruct {

    GoodsMapStruct INSTANCE = Mappers.getMapper(GoodsMapStruct.class);

    /**
     * 商品DTO->VO
     *
     * @param goodsDTO 商品数据传输对象
     * @return 商品视图对象
     */
    GoodsVo convert(GoodsDTO goodsDTO);

    /**
     * GoodsDO -> SeckillGoods
     *
     * @param goodsDTO 商品DO
     * @return 秒杀商品
     */
    @Mapping(source = "goodsId", target = "goodsId")
    SeckillGoods toSeckillGoods(GoodsDTO goodsDTO);

    /**
     * 商品DTO列表->商品VO列表
     *
     * @param goodsDTOList DTO列表
     * @return VO列表
     */
    List<GoodsVo> convertListDTOtoVO(List<GoodsDTO> goodsDTOList);

}
