package com.plusl.core.facade.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "秒杀订单ID不能为空")
    private Long id;

    /**
     * 商品ID
     */
    @NotBlank(message = "商品ID不能为空")
    private Long goodsId;

    /**
     * 秒杀商品库存
     */
    @NotBlank(message = "商品库存不能为空")
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
