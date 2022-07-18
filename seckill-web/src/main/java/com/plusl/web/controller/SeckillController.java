package com.plusl.web.controller;

import com.plusl.common.entity.OrderInfo;
import com.plusl.common.entity.SeckillOrder;
import com.plusl.common.entity.User;
import com.plusl.common.enums.result.Result;
import com.plusl.common.enums.status.ResultStatus;
import com.plusl.common.vo.GoodsVo;
import com.plusl.common.vo.SeckillMessageVo;
import com.plusl.service.GoodsService;
import com.plusl.service.OrderService;
import com.plusl.service.SeckillService;
import com.plusl.service.redis.GoodsKey;
import com.plusl.service.redis.RedisService;
import com.plusl.service.rocketmq.MqProducer;
import com.plusl.web.interceptor.RequireLogin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.plusl.common.enums.status.ResultStatus.EXCEPTION;
import static com.plusl.common.enums.status.ResultStatus.MIAO_SHA_OVER;

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
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    SeckillService sekcillService;

    @Autowired
    MqProducer mqProducer;

    @Autowired
    OrderService orderService;

    /**
     * redis停止请求标识
     */
    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 执行秒杀，异步请求秒杀
     *
     * @param user    用户实体
     * @param goodsId 商品ID
     * @return 规范化Json返回值
     */
    @RequireLogin(seconds = 5, maxCount = 1000, needLogin = false)
    @RequestMapping(value = "/doseckill", method = RequestMethod.POST)
    public Result<OrderInfo> doSeckill(User user, @RequestParam("goodsId") long goodsId) {

        Result<OrderInfo> result = Result.build();

        //判断是否已经秒杀到了,防止重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            result.withError(ResultStatus.REPEATE_SECKILL);
            return result;
        }

        //内存标识符，防止库存结束后仍然查询redis
        Boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            result.withError(MIAO_SHA_OVER);
            return result;
        }

        //预减库存
        Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.replace(goodsId, true);
            result.withError(EXCEPTION.getCode(), MIAO_SHA_OVER.getMessage());
            return result;
        }

        SeckillMessageVo seckillMessageVo = new SeckillMessageVo();
        seckillMessageVo.setGoodsId(goodsId);
        seckillMessageVo.setUser(user);
        mqProducer.sendSeckillMessage(seckillMessageVo);
        return result;
    }

    @RequireLogin(seconds = 5, maxCount = 5, needLogin = false)
    @RequestMapping(value = "/result", method = RequestMethod.GET)
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
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsList) {
            goodsService.initSetGoodsMock(goodsVo);
            localOverMap.put(goodsVo.getId(), false);
        }
    }
}

