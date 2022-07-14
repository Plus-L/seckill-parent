# SecKill-PlusL
秒杀系统 Designed By PlusL，初版为若鱼老师的高并发秒杀系统课程，自己做了许多更新。
技术栈如下：Dubbo、SpringBoot、Mybatis、RocketMq、Zookeeper、Sentinel等
主要关注后端接口，前端只做了简单处理

# 整体结构

- seckill-parent 「父文件夹」

- - seckill-common 「存放普通数据，如实体entity，枚举enum，工具类utils等等」

- - - entity
    - enum
    - utils
    - result
    - exception

- - seckill-service  「服务层，主要进行逻辑业务处理」

- - - mapper
    - service
    - 各数据库管理/中间件业务管理

- - seckill-web  「Web端，提供接口处理，以及静态页面」

- - - config
    - controller
    - interceptor
    - resources

- - - - template

（更新中）

