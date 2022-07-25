package com.plusl.core.facade.impl;

import cn.hutool.core.util.ObjectUtil;
import com.plusl.core.facade.api.SeckillFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.service.SeckillService;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.framework.common.entity.OrderInfo;
import com.plusl.framework.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.plusl.framework.common.enums.status.ResultStatus.GET_RESULT_ERROR;
import static com.plusl.framework.common.enums.status.ResultStatus.SECKILL_FAIL;

/**
 * @program: seckill-framework
 * @description: SeckillFacade实现类
 * @author: PlusL
 * @create: 2022-07-23 16:23
 **/
@Slf4j
@Component
@DubboService(version = "1.0.0")
public class SeckillFacadeImpl implements SeckillFacade {

    @Autowired
    SeckillService seckillService;

    @Override
    public FacadeResult<OrderInfo> createOrderAndReduceStock(User user, GoodsDTO goodsDTO) {

        try{
            OrderInfo info = seckillService.createOrderAndReduceStock(user, goodsDTO);
            if (ObjectUtil.isEmpty(info)) {
                return FacadeResult.fail(SECKILL_FAIL.getCode(), SECKILL_FAIL.getMessage());
            }
            return FacadeResult.success(info);
        } catch (Exception e) {
            log.warn("方法 [createOrderAndReduceStock] 异常 异常信息：", e);
            return FacadeResult.fail(SECKILL_FAIL.getCode(), SECKILL_FAIL.getMessage());
        }
    }

    @Override
    public FacadeResult<Long> getSeckillResult(Long userId, Long goodsId) {
        try{
            Long seckillResult = seckillService.getSeckillResult(userId, goodsId);
            return FacadeResult.success(seckillResult);
        } catch (Exception e) {
            log.warn("方法 [getSeckillResult] 异常 异常信息：", e);
            return FacadeResult.fail(GET_RESULT_ERROR.getCode(), GET_RESULT_ERROR.getMessage());
        }
    }

}
