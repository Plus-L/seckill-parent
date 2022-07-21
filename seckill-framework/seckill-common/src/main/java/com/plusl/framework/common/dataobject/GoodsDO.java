package com.plusl.framework.common.dataobject;

import com.plusl.framework.common.entity.Goods;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * @program: seckill-parent
 * @description: 商品数据对象
 * @author: PlusL
 * @create: 2022-07-18 16:43
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Alias("goodsDo")
public class GoodsDO extends Goods {

    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
