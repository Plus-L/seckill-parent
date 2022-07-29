package com.plusl.web.controller;

import com.plusl.framework.common.convert.goods.GoodsMapStruct;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.framework.common.vo.GoodsDetailVo;
import com.plusl.framework.common.vo.GoodsVo;
import com.plusl.web.client.GoodsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: seckill-parent
 * @description: 商品控制器
 * @author: PlusL
 * @create: 2022-07-07 09:39
 **/
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
    public CommonResult<GoodsDetailVo> getGoodsDetail(User user, @PathVariable("goodsId") Long goodsId) {

        GoodsVo goodsVo = GoodsMapStruct.INSTANCE.convert(goodsClient.getGoodsDTOByGoodsId(goodsId));

        // 判断距离秒杀开始还有多少时间
        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;
        if (now < startTime) {
            // 秒杀还没开始，倒计时
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {
            // 秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            seckillStatus = 1;
        }

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goodsVo);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSeckillStatus(seckillStatus);
        return CommonResult.success(vo);
    }


}
