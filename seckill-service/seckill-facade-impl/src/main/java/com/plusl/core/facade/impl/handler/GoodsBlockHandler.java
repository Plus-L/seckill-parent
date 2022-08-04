package com.plusl.core.facade.impl.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.plusl.framework.common.enums.status.ResultStatus.NET_BUSY;

/**
 * @program: seckill-facade-api
 * @description: 商品blockHandlerClass
 * @author: PlusL
 * @create: 2022-08-03 11:36
 **/
@Slf4j
public class GoodsBlockHandler {

    public FacadeResult<List<GoodsDTO>> blockHandlerForGetGoodsDTOList(BlockException e) {
        FacadeResult<List<GoodsDTO>> facadeResult = fail();
        log.warn("获取商品列表信息 触发流控 返回信息 : {}", JSON.toJSONString(facadeResult), e);
        return facadeResult;
    }

    public FacadeResult<GoodsDTO> blockHandlerForgetGoodsDtoByGoodsId(Long goodsId, BlockException e) {
        FacadeResult<GoodsDTO> facadeResult = fail();
        log.warn("获取商品列表信息 触发流控 请求信息 : {} 返回信息 : {}", JSON.toJSONString(goodsId), JSON.toJSONString(facadeResult), e);
        return facadeResult;
    }

    public FacadeResult<Boolean> blockHandlerForReduceStockByGoodsId(Long goodsId, BlockException e) {
        FacadeResult<Boolean> facadeResult = fail();
        log.warn("通过商品ID削减库存 触发流控 请求信息 : {}  返回信息 : {}", JSON.toJSONString(goodsId), JSON.toJSONString(facadeResult), e);
        return facadeResult;
    }

    private FacadeResult fail() {
        FacadeResult result = new FacadeResult();
        result.setErrorCode(NET_BUSY.getCode());
        result.setMessage(NET_BUSY.getMessage());
        return result;
    }
}

