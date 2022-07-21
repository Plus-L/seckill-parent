package com.plusl.framework.common.utils;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

    /**
     * @param request 请求
     * @return ua
     */
    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }
}
