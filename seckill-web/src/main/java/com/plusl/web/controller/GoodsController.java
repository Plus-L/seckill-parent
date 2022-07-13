package com.plusl.web.controller;

import com.plusl.common.entity.User;
import com.plusl.common.enums.result.Result;
import com.plusl.common.vo.GoodsDetailVo;
import com.plusl.common.vo.GoodsVo;
import com.plusl.service.GoodsService;
import com.plusl.service.redis.GoodsKey;
import com.plusl.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: seckill-parent
 * @description: 商品控制器
 * @author: PlusL
 * @create: 2022-07-07 09:39
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;


    @RequestMapping(value = "/to_list", produces = "text/html")
    public String getGoodsList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return render(request, response, model, "goods_list", GoodsKey.getGoodsList, "");
    }

    @RequestMapping("/detail/{goodsId}")
    public Result<GoodsDetailVo> getGoodsDetail(User user, @PathVariable("goodsId") Long goodsId) {

        Result<GoodsDetailVo> result = Result.build();

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);

        //判断距离秒杀开始还有多少时间
        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;
        if (now < startTime) {
            //秒杀还没开始，倒计时
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {
            //秒杀已经结束
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
        vo.setMiaoshaStatus(seckillStatus);
        result.setData(vo);
        return result;
    }


}
