package com.plusl.web.controller;

import cn.hutool.core.util.ObjectUtil;
import com.plusl.core.facade.api.entity.OrderInfo;
import com.plusl.core.facade.api.entity.User;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.web.client.GoodsClient;
import com.plusl.web.client.OrderClient;
import com.plusl.web.mapstruct.GoodsMapStruct;
import com.plusl.web.vo.GoodsVo;
import com.plusl.web.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.plusl.framework.common.enums.status.ResultStatus.ORDER_NOT_EXIST;
import static com.plusl.framework.common.enums.status.ResultStatus.SESSION_ERROR;

/**
 * @program: seckill-parent
 * @description: 订单控制类
 * @author: PlusL
 * @create: 2022-07-08 11:41
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderClient orderClient;

    @Autowired
    GoodsClient goodsClient;

    /**
     * 获取详细订单信息
     *
     * @param user    用户实体
     * @param orderId 订单ID
     * @return CommonResult 封装订单信息VO
     */
    @GetMapping("/detail")
    public CommonResult<OrderDetailVo> orderDetailInfo(@RequestBody User user, @RequestParam("orderId") long orderId) {

        if (ObjectUtil.isEmpty(user)) {
            return CommonResult.error(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
        }

        OrderInfo order = orderClient.getOrderById(orderId);
        if (ObjectUtil.isEmpty(order)) {
            return CommonResult.error(ORDER_NOT_EXIST.getCode(), ORDER_NOT_EXIST.getMessage());
        }

        long goodsId = order.getGoodsId();
        GoodsVo goods = GoodsMapStruct.INSTANCE.convert(goodsClient.getGoodsDtoByGoodsId(goodsId));

        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoods(goods);

        return CommonResult.success(orderDetailVo);
    }

}
