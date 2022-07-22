package com.plusl.web.controller;

import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.SeckillOrder;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.enums.result.Result;
import com.plusl.framework.common.enums.status.ResultStatus;
import com.plusl.framework.common.redis.GoodsKey;
import com.plusl.framework.common.dto.SeckillMessageDTO;
import com.plusl.framework.common.redis.RedisUtil;
import com.plusl.core.service.Interface.GoodsService;
import com.plusl.core.service.Interface.OrderService;
import com.plusl.core.service.Interface.SeckillService;
import com.plusl.core.service.rocketmq.MqProducer;
import com.plusl.web.interceptor.RequireLogin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.plusl.framework.common.enums.status.ResultStatus.EXCEPTION;
import static com.plusl.framework.common.enums.status.ResultStatus.MIAO_SHA_OVER;

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
    GoodsService goodsService;

    @Autowired
    SeckillService sekcillService;

    @Autowired
    MqProducer mqProducer;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 执行秒杀，异步请求秒杀
     *
     * @param user    用户实体
     * @param goodsId 商品ID
     * @return 规范化Json返回值
     */
    @RequireLogin(seconds = 5, maxCount = 1000, needLogin = false)
    @PostMapping(value = "/doseckill")
    public Result<OrderInfo> doSeckill(@RequestBody User user, @RequestParam("goodsId") Long goodsId) {

        Result<OrderInfo> result = Result.build();
        //判断是否已经秒杀到了,防止重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            result.withError(ResultStatus.REPEATE_SECKILL);
            return result;
        }

        //预减库存
        Long stock = redisUtil.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if (stock < 0) {
            result.withError(EXCEPTION.getCode(), MIAO_SHA_OVER.getMessage());
            return result;
        }

        SeckillMessageDTO seckillMessageDTO = new SeckillMessageDTO();
        seckillMessageDTO.setGoodsId(goodsId);
        seckillMessageDTO.setUser(user);
        mqProducer.sendSeckillMessage(seckillMessageDTO);
        return result;
    }

    @RequireLogin(seconds = 5, maxCount = 5, needLogin = false)
    @GetMapping(value = "/result")
    public Result<Long> getSeckillResult(Model model, User user,
                                         @RequestParam("goodsId") long goodsId) {
        Result<Long> result = Result.build();
        model.addAttribute("user", user);
        Long seckillResult = sekcillService.getSeckillResult(user.getId(), goodsId);
        result.setData(seckillResult);
        return result;
    }

    /**
     * 系统初始化时执行，启动时将数据库中的秒杀商品数量加载到缓存中来
     *
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsDTO> goodsList = goodsService.listGoodsDTO();
        if (goodsList == null) {
            return;
        }
        for (GoodsDTO goodsDTO : goodsList) {
            goodsService.initSetGoodsMock(goodsDTO);
        }
    }
}

