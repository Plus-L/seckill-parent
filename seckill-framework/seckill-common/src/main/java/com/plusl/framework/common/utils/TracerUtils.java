package com.plusl.framework.common.utils;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;

/**
 * @program: seckill-parent
 * @description:
 * @author: PlusL
 * @create: 2022-07-20 16:32
 **/
public class TracerUtils {

    /**
     * 私有化构造方法
     */
    private TracerUtils() {
    }

    /**
     * 获得链路追踪编号，直接返回 SkyWalking 的 TraceId。
     * 如果不存在的话为空字符串！！！
     *
     * @return 链路追踪编号
     */
    public static String getTraceId() {
        return TraceContext.traceId();
    }

}
