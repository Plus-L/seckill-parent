package com.plusl.core.service.impl;

import com.plusl.core.service.GoodsService;
import com.plusl.framework.common.dto.GoodsDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * @program: seckill-facade-api
 * @description: mock测试
 * @author: PlusL
 * @create: 2022-08-01 14:38
 **/
class GoodsServiceImplTest {

    @Spy
    private GoodsService goodsService;

    @BeforeEach
    void setUp() {
        System.out.println("--测试前准备--");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listGoodsDTO() {
//        Mockito.when(goodsService.listGoodsDTO()).thenReturn();
//        Assertions.assertEquals(, goodsService.listGoodsDTO());
        System.out.println(goodsService.listGoodsDTO());
    }

    @Test
    void getGoodsDoByGoodsId() {
        GoodsDTO goodsDTO = new GoodsDTO();
//        goodsDTO.setGoodsId();
        // when即打桩，当执行when内指定的方法时，会返回指定的输出结果（即thenReturn后的结果）
//        Mockito.when(goodsService.getGoodsDoByGoodsId(106210002L)).thenReturn(goodsDTO);

//        Assertions.assertEquals(goodsDTO, goodsService.getGoodsDtoByGoodsId(106210002L));

        System.out.println(goodsService.getGoodsDtoByGoodsId(106210002L));
    }

    @Test
    void reduceOneStock() {
    }

    @Test
    void initSetGoodsMock() {
    }

    @Test
    void delStockCountCache() {
    }
}
