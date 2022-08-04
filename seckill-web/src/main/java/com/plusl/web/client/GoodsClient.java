package com.plusl.web.client;

import com.plusl.core.facade.api.GoodsFacade;
import com.plusl.core.facade.api.entity.FacadeResult;
import com.plusl.core.facade.api.entity.dto.GoodsDTO;
import com.plusl.web.utils.Assert;
import com.plusl.web.vo.GoodsDetailVo;
import com.plusl.web.vo.GoodsVo;
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

    public GoodsDTO getGoodsDtoByGoodsId(Long goodsId) {
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

    public GoodsDetailVo getStatusAndRemainSeconds(GoodsVo goodsVo) {
        // 判断距离秒杀开始还有多少时间
        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;
        if (now < startTime) {
            // 秒杀还没开始，倒计时
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {
            // 秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            seckillStatus = 1;
        }

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goodsVo);
        vo.setRemainSeconds(remainSeconds);
        vo.setSeckillStatus(seckillStatus);
        return vo;
    }
}
