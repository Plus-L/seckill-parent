package com.plusl.web.controller;

import cn.hutool.core.util.ObjectUtil;
import com.plusl.framework.common.convert.goods.GoodsMapStruct;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.User;
import com.plusl.framework.common.enums.result.CommonResult;
import com.plusl.framework.common.enums.result.Result;
import com.plusl.framework.common.enums.status.ResultStatus;
import com.plusl.framework.common.vo.GoodsVo;
import com.plusl.framework.common.vo.OrderDetailVo;
import com.plusl.web.client.GoodsClient;
import com.plusl.web.client.OrderClient;
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
        GoodsVo goods = GoodsMapStruct.INSTANCE.convert(goodsClient.getGoodsDTOByGoodsId(goodsId));

        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoods(goods);

        return CommonResult.success(orderDetailVo);
    }

}
