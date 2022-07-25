package com.plusl.core.facade.impl;

import cn.hutool.core.util.ObjectUtil;
import com.plusl.core.facade.api.GoodsFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.service.GoodsService;
import com.plusl.framework.common.dto.GoodsDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.plusl.framework.common.enums.status.ResultStatus.*;

/**
 * @program: seckill-framework
 * @description: 商品Facade实现类
 * @author: PlusL
 * @create: 2022-07-25 10:06
 **/
@Slf4j
@Component
@DubboService(version = "1.0.0")
public class GoodsFacadeImpl implements GoodsFacade {

    @Autowired
    GoodsService goodsService;

    @Override
    public FacadeResult<List<GoodsDTO>> getGoodsDTOList() {

        try {
            List<GoodsDTO> goodsDTOList = goodsService.listGoodsDTO();
            if (ObjectUtil.isEmpty(goodsDTOList)) {
                return FacadeResult.fail(GET_GOODS_LIST_ERROR.getCode(), GET_GOODS_LIST_ERROR.getMessage());
            }
            return FacadeResult.success(goodsDTOList);
        } catch (Exception e) {
            log.warn("方法 [getGoodsDTOList] 异常 异常信息：", e);
            return FacadeResult.fail(GET_GOODS_LIST_ERROR.getCode(), GET_GOODS_LIST_ERROR.getMessage());
        }
    }

    @Override
    public FacadeResult<GoodsDTO> getGoodsDTOByGoodsId(Long goodsId) {

        try {
            GoodsDTO goodsDTO = goodsService.getGoodsDoByGoodsId(goodsId);
            if (ObjectUtil.isEmpty(goodsDTO)) {
                return FacadeResult.fail(GET_GOODS_ERROR.getCode(), GET_GOODS_ERROR.getMessage());
            }
            return FacadeResult.success(goodsDTO);
        } catch (Exception e) {
            log.warn("方法 [getGoodsDTOByGoodsId] 异常 异常信息：", e);
            return FacadeResult.fail(GET_GOODS_ERROR.getCode(), GET_GOODS_ERROR.getMessage());
        }
    }

    @Override
    public FacadeResult<Boolean> reduceStockByGoodsId(Long goodsId) {
        Boolean isOk = goodsService.reduceStock(goodsId);
        if (isOk) {
            return FacadeResult.success(true);
        } else {
            log.warn("方法 [reduceStockByGoodsId] 削减库存失败");
            return FacadeResult.fail(PURCHASE_FAIL.getCode(), PURCHASE_FAIL.getMessage());
        }
    }

    @Override
    public FacadeResult<Boolean> initSetGoodsMock(GoodsDTO goodsDTO) {
        Boolean isOk = goodsService.initSetGoodsMock(goodsDTO);
        if (isOk) {
            return FacadeResult.success(true);
        } else {
            log.warn("方法 [initSetGoodsMock] 初始化将库存加载到缓存中失败");
            return FacadeResult.fail(INIT_MOCK_ERROR.getCode(), INIT_MOCK_ERROR.getMessage());
        }
    }

}
