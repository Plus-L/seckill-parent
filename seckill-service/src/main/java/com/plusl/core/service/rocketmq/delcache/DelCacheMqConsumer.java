package com.plusl.core.service.rocketmq.delcache;

import com.plusl.core.service.GoodsService;
import com.plusl.framework.common.exception.GlobalException;
import com.plusl.framework.common.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.plusl.framework.common.enums.status.ResultStatus.SEND_FAIL;

/**
 * @program: seckill-parent
 * @description:
 * @author: PlusL
 * @create: 2022-07-27 11:14
 **/
@Slf4j
@Service
@RocketMQMessageListener(topic = "delcache_queue", consumerGroup = "delcache_queue")
public class DelCacheMqConsumer implements RocketMQListener<String> {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    GoodsService goodsService;

    @Override
    public void onMessage(String s) {
        Long goodsId = NumberUtils.toLong(s);
        log.info("DelCache-queue receive message : " + goodsId);
        Boolean isSuccess = goodsService.delStockCountCache(goodsId);
        if (!isSuccess) {
            log.warn("消息队列删除商品缓存失败 接收信息 : {}", s);
            throw new GlobalException(SEND_FAIL.getCode(), SEND_FAIL.getMessage());
        }
    }
}
