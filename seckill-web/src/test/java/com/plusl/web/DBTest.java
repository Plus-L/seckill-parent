package com.plusl.web;

import com.alibaba.fastjson.JSONObject;
import com.plusl.framework.common.entity.User;
import com.plusl.core.service.mapper.GoodsMapper;
import com.plusl.core.service.mapper.UserMapper;
import com.plusl.framework.common.dataobject.GoodsDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

/**
 * @program: seckill-parent
 * @description: 数据库测试
 * @author: PlusL
 * @create: 2022-07-06 17:26
 **/
@SpringBootTest
@ContextConfiguration(classes = SeckillWebApplication.class)
public class DBTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testMySQLDB() {
        User user0 = userMapper.getByNickname("user0");
        System.out.println(JSONObject.toJSONString(user0));

        GoodsDO goodsDoByGoodsId = goodsMapper.getGoodsDoByGoodsId(1);
        System.out.println(JSONObject.toJSONString(goodsDoByGoodsId));

//        List<GoodsVo> list = goodsService.listGoodsVo();
//        System.out.println(list);
    }

    @Test
    public void testUserService() {
        redisTemplate.opsForValue().set(1, 100);
        System.out.println(redisTemplate.opsForValue().get(1));
    }
}
