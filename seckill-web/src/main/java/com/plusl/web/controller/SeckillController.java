package com.plusl.web.controller;

import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.core.facade.api.entity.dto.SeckillMessageDTO;
import com.plusl.core.facade.api.entity.User;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.framework.common.enums.status.ResultStatus;
import com.plusl.web.client.GoodsClient;
import com.plusl.web.client.OrderClient;
import com.plusl.web.client.RocketMqClient;
import com.plusl.web.client.SeckillClient;
import com.plusl.web.interceptor.RequireLogin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

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


    /**
     * 执行秒杀，异步请求秒杀
     * RequireLogin限制了两点：1.单一用户单位时间内能点击的次数限制 2.当前接口是否需要登录才能访问
     *
     * @param user    用户实体
     * @param goodsId 商品ID
     * @return 规范化Json返回值
     */
    @RequireLogin
    @PostMapping(value = "/doseckill")
    public CommonResult<String> doSeckill(@RequestBody User user, @RequestParam("goodsId") Long goodsId) {

        //判断是否已经秒杀到了,防止重复秒杀
/*        SeckillOrder order = orderClient.preventRepeatedSeckill(user.getId(), goodsId);
        if (order != null) {
            return CommonResult.error(REPEATE_SECKILL);
        }*/

        SeckillMessageDTO seckillMessageDTO = new SeckillMessageDTO();
        seckillMessageDTO.setGoodsId(goodsId);
        seckillMessageDTO.setUser(user);
        String seckillMessage = rocketMqClient.sendSeckillMessage(seckillMessageDTO);
        return CommonResult.success(seckillMessage);
    }

    /**
     * 获取秒杀结果
     *
     * @param user    用户实体
     * @param goodsId 商品Id
     * @return 秒杀结果 成功返回订单ID  失败：（1）.商品库存空失败-返回0 （2）.异常导致失败-返回-1
     */
    @RequireLogin
    @GetMapping(value = "/result")
    public CommonResult<String> getSeckillResult(User user, @RequestParam("goodsId") long goodsId) {
        Long resultCode = seckillClient.getSeckillResult(user.getId(), goodsId);
        if (resultCode.equals(0L)) {
            return CommonResult.error(ResultStatus.SECKILL_OVER);
        }
        if (resultCode.equals(-1L)) {
            return CommonResult.error(ResultStatus.SECKILL_FAIL);
        }

        return CommonResult.success(ResultStatus.SECKILL_SUCCESS.getMessage());
    }

    /**
     * 系统初始化时执行，启动时将数据库中的秒杀商品数量加载到缓存中来
     */
    @PostConstruct
    public void afterPropertiesSet() {
        List<GoodsDTO> goodsList = goodsClient.getGoodsDTOList();
        for (GoodsDTO goodsDTO : goodsList) {
            goodsClient.initSetGoodsMock(goodsDTO);
        }
    }
}

