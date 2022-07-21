package com.plusl.framework.common.vo;

import com.plusl.framework.common.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @program: seckill-parent
 * @description: 商品VO
 * @author: PlusL
 * @create: 2022-07-07 11:28
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {

    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

}
