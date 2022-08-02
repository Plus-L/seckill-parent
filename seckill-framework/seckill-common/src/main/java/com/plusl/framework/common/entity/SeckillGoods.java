package com.plusl.framework.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Alias("SeckillGoods")
public class SeckillGoods implements Serializable {

    /**
     * 秒杀商品ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 秒杀商品库存
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private Date startDate;

    /**
     * 秒杀结束时间
     */
    private Date endDate;
}
