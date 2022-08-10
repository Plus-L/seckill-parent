package com.plusl.web.controller;


import cn.hutool.core.util.ObjectUtil;
import com.plusl.core.facade.api.entity.SeckillOrder;
import com.plusl.core.facade.api.entity.User;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.core.facade.api.entity.dto.SeckillMessageDTO;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.web.client.GoodsClient;
import com.plusl.web.client.OrderClient;
import com.plusl.web.client.RocketMqClient;
import com.plusl.web.client.SeckillClient;
import com.plusl.web.interceptor.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

import static com.plusl.framework.common.enums.status.ResultStatus.*;
import static com.plusl.framework.common.constant.CommonConstant.*;

/**
 * @program: seckill-parent
 * @description: 秒杀控制类
 * @author: PlusL
 * @create: 2022-07-07 15:18
 **/
@RestController
@RequestMapping("/activity")
public class SeckillController {

    @Autowired
    GoodsClient goodsClient;

    @Autowired
    SeckillClient seckillClient;

    @Autowired
    RocketMqClient rocketMqClient;

    @Autowired
    OrderClient orderClient;

    /**
     * 执行秒杀，异步请求秒杀
     * RequireLogin限制了: 该接口需要登录后才能访问
     *
     * 1. 商品有无库存 2. 判断有没有重复秒杀  3. 发送到mq
     *
     * 注意返回一个单据号给前端，方便前端查询。此时获取秒杀结果时可以通过单据和用户查
     *
     * @param user    用户实体
     * @param goodsId 商品ID
     * @return 规范化Json返回值
     */
    @RequireLogin
    @PostMapping(value = "/doSeckill")
    public CommonResult<String> doSeckill(@RequestBody User user, @RequestParam("goodsId") Long goodsId) {

        // 判断商品有无库存
        GoodsDTO goodsDTO = goodsClient.getGoodsDtoByGoodsId(goodsId);
        if (goodsDTO.getStockCount() <= 0) {
            return CommonResult.error(SECKILL_OVER);
        }

        //判断是否已经秒杀到了,防止重复秒杀
        SeckillOrder order = orderClient.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return CommonResult.error(REPEATE_SECKILL);
        }

        SeckillMessageDTO seckillMessageDTO = new SeckillMessageDTO();
        seckillMessageDTO.setGoodsId(goodsId);
        seckillMessageDTO.setUser(user);
        String seckillMessage = rocketMqClient.sendSeckillMessage(seckillMessageDTO);
        if (ObjectUtil.isNull(seckillMessage)){
            return CommonResult.error(SEND_FAIL);
        }
        return CommonResult.success(seckillMessage);
    }

    /**
     * 获取秒杀结果
     * 注意当用户请求如果拿不到结果的话，用户的体验不佳
     *
     * @param user    用户实体
     * @param goodsId 商品Id
     * @return 秒杀结果 成功返回订单ID  失败：（1）.商品库存空失败-返回0 （2）.异常导致失败-返回-1
     */
    @RequireLogin
    @GetMapping(value = "/result")
    public CommonResult<String> getSeckillResult(User user, @RequestParam("goodsId") long goodsId) {
        String resultMessage = seckillClient.getSeckillResult(user.getId(), goodsId);

        if (resultMessage.equals(SECKILL_IN_LINE)) {
            return CommonResult.error(SEND_SUCCESS);
        }
        if (resultMessage.equals(SECKILL_SOLD_OUT)) {
            return CommonResult.error(SECKILL_OVER);
        }
        return CommonResult.success(resultMessage);
    }

    /**
     * 系统初始化时执行，启动时将数据库中的秒杀商品数量加载到缓存中来
     *
     * 加载商品缓存的要放到Service中去，不能在Controller中处理事务
     */
    @PostConstruct
    public void afterPropertiesSet() {
        List<GoodsDTO> goodsList = goodsClient.getGoodsDTOList();
        for (GoodsDTO goodsDTO : goodsList) {
            goodsClient.initSetGoodsMock(goodsDTO);
        }
    }
}

