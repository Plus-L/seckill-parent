package com.plusl.core.facade.api.entity.dto;

import com.plusl.core.facade.api.entity.Goods;
import lombok.*;

import javax.validation.constraints.NotNull;
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
    @NotNull(message = "秒杀价格不能为空")
    private Double seckillPrice;

    /**
     * 秒杀商品库存
     */
    @NotNull(message = "秒杀库存不能为空")
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
