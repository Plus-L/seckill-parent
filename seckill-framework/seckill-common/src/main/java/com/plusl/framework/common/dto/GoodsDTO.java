package com.plusl.framework.common.dto;

import com.plusl.framework.common.entity.Goods;
import lombok.*;

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
public class GoodsDTO extends Goods {
    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
