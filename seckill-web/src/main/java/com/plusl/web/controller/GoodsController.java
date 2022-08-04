package com.plusl.web.controller;

import com.plusl.core.facade.api.entity.User;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.web.vo.GoodsDetailVo;
import com.plusl.web.vo.GoodsVo;
import com.plusl.web.client.GoodsClient;
import com.plusl.web.mapstruct.GoodsMapStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: seckill-parent
 * @description: 商品控制器
 * @author: PlusL
 * @create: 2022-07-07 09:39
 **/
@Validated
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsClient goodsClient;

    @GetMapping(value = "/to_list")
    public CommonResult<List<GoodsVo>> getGoodsList() {
        List<GoodsVo> goodsList = GoodsMapStruct.INSTANCE.convertListDTOtoVO(goodsClient.getGoodsDTOList());
        return CommonResult.success(goodsList);
    }

    @GetMapping("/detail/{goodsId}")
    public CommonResult<GoodsDetailVo> getGoodsDetail(@Valid User user, @PathVariable("goodsId") Long goodsId) {

        GoodsVo goodsVo = GoodsMapStruct.INSTANCE.convert(goodsClient.getGoodsDtoByGoodsId(goodsId));

        GoodsDetailVo vo = goodsClient.getStatusAndRemainSeconds(goodsVo);
        vo.setUser(user);

        return CommonResult.success(vo);
    }


}
