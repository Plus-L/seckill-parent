package com.plusl.core.service.dataobject;

import com.plusl.core.facade.api.entity.Goods;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
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
public class GoodsDO extends Goods implements Serializable {

    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
