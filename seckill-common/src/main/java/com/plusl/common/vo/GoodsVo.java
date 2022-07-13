package com.plusl.common.vo;

import com.plusl.common.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

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
@Alias("goodsVo")
public class GoodsVo extends Goods {

    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

}
