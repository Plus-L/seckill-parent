package com.plusl.framework.common.dto;

import com.plusl.framework.common.entity.Goods;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: seckill-parent
 * @description: 商品数据传输对象
 * @author: PlusL
 * @create: 2022-07-18 17:37
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoodsDTO extends Goods implements Serializable {
    /**
     * 秒杀价格
     */
    private Double seckillPrice;

    /**
     * 秒杀商品库存
     */
    private Integer stockCount;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;
}
