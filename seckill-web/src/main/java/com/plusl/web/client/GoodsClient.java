package com.plusl.web.client;

import com.plusl.core.facade.api.GoodsFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.framework.common.dto.GoodsDTO;
import com.plusl.web.utils.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: seckill-framework
 * @description: 商品Client
 * @author: PlusL
 * @create: 2022-07-25 09:29
 **/
@Service
public class GoodsClient {

    @DubboReference(version = "1.0.0", timeout = 30000, check = false, url = "localhost:20880")
    GoodsFacade goodsFacade;

    public List<GoodsDTO> getGoodsDTOList() {
        FacadeResult<List<GoodsDTO>> result = goodsFacade.getGoodsDTOList();
        Assert.isSuccess(result);
        return result.getData();
    }

    public GoodsDTO getGoodsDTOByGoodsId(Long goodsId) {
        FacadeResult<GoodsDTO> result = goodsFacade.getGoodsDtoByGoodsId(goodsId);
        Assert.isSuccess(result);
        return result.getData();
    }

    public Boolean reduceStockByGoodsId(Long goodsId) {
        FacadeResult<Boolean> result = goodsFacade.reduceStockByGoodsId(goodsId);
        Assert.isSuccess(result);
        return result.getData();
    }

    public Boolean initSetGoodsMock(GoodsDTO goodsDTO) {
        FacadeResult<Boolean> result = goodsFacade.initSetGoodsMock(goodsDTO);
        Assert.isSuccess(result);
        return result.getData();
    }

}
