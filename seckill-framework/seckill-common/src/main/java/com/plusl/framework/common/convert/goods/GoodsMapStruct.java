package com.plusl.framework.common.convert.goods;


import com.plusl.framework.common.dataobject.GoodsDO;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.vo.GoodsVo;
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
@Mapper()
public interface GoodsMapStruct {

    GoodsMapStruct INSTANCE = Mappers.getMapper(GoodsMapStruct.class);

    /**
     * 商品DO->DTO
     * @param goodsDO 商品数据对象
     * @return 商品数据传输对象
     */
    GoodsDTO convert(GoodsDO goodsDO);

    /**
     * 商品DTO->VO
     * @param goodsDTO 商品数据传输对象
     * @return 商品视图对象
     */
    GoodsVo convert(GoodsDTO goodsDTO);

    /**
     * 商品DO列表->DTO列表
     * @param goodsDOList DO列表
     * @return DTO列表
     */
    List<GoodsDTO> convertListDOtoDTO(List<GoodsDO> goodsDOList);

    List<GoodsVo> convertListDTOtoVO(List<GoodsDTO> goodsDTOList);


}
