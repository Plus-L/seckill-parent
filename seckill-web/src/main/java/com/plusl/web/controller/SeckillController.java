package com.plusl.web.controller;

import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.dto.SeckillMessageDTO;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.framework.common.redis.GoodsKey;
import com.plusl.framework.common.redis.RedisUtil;
import com.plusl.web.client.GoodsClient;
import com.plusl.web.client.OrderClient;
import com.plusl.web.client.RocketMqClient;
import com.plusl.web.client.SeckillClient;
import com.plusl.web.interceptor.RequireLogin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.plusl.framework.common.enums.status.ResultStatus.REPEATE_SECKILL;
import static com.plusl.framework.common.enums.status.ResultStatus.SECKILL_OVER;

/**
 * @program: seckill-parent
 * @description: 秒杀控制类
 * @author: PlusL
 * @create: 2022-07-07 15:18
 **/
@RestController
@RequestMapping("/activity")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsClient goodsClient;

    @Autowired
    SeckillClient seckillClient;

    @Autowired
    RocketMqClient rocketMqClient;

    @Autowired
    OrderClient orderClient;

//    @Autowired
//    RedisUtil redisUtil;

    /**
     * 执行秒杀，异步请求秒杀
     * RequireLogin限制了两点：1.单一用户单位时间内能点击的次数限制 2.当前接口是否需要登录才能访问
     *
     * @param user    用户实体
     * @param goodsId 商品ID
     * @return 规范化Json返回值
     */
    @RequireLogin(seconds = 5, maxCount = 5, needLogin = false)
    @PostMapping(value = "/doseckill")
    public CommonResult<String> doSeckill(@RequestBody User user, @RequestParam("goodsId") Long goodsId) {

        //判断是否已经秒杀到了,防止重复秒杀
        SeckillOrder order = orderClient.preventRepeatedSeckill(user.getId(), goodsId);
        if (order != null) {
            return CommonResult.error(REPEATE_SECKILL);
        }

        //TODO: 如何保证数据库与缓存的一致性
        //预减库存
//        Long stock = redisUtil.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
//        if (stock < 0) {
//            return CommonResult.error(SECKILL_OVER);
//        }

        SeckillMessageDTO seckillMessageDTO = new SeckillMessageDTO();
        seckillMessageDTO.setGoodsId(goodsId);
        seckillMessageDTO.setUser(user);
        String seckillMessage = rocketMqClient.sendSeckillMessage(seckillMessageDTO);
        return CommonResult.success(seckillMessage);
    }

    @RequireLogin(seconds = 5, maxCount = 5, needLogin = false)
    @GetMapping(value = "/result")
    public CommonResult<Long> getSeckillResult(Model model, User user,
                                         @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        Long seckillResult = seckillClient.getSeckillResult(user.getId(), goodsId);
        return CommonResult.success(seckillResult);
    }

    /**
     * 系统初始化时执行，启动时将数据库中的秒杀商品数量加载到缓存中来
     */
    @Override
    public void afterPropertiesSet(){
        List<GoodsDTO> goodsList = goodsClient.getGoodsDTOList();
        for (GoodsDTO goodsDTO : goodsList) {
            goodsClient.initSetGoodsMock(goodsDTO);
        }
    }
}

