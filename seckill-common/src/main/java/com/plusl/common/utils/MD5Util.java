package com.plusl.common.utils;


import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;

/**
 * @program: seckill-parent
 * @description: MD5工具类
 * @author: PlusL
 * @create: 2022-07-05 17:09
 **/
public class MD5Util {

    private static String getSalt = "1a2b3c4d";

    public static String MD5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static final String getSaltT() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[15];
        random.nextBytes(bytes);
        String salt = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
        return salt;
    }

    /**
     * 盐值salt 随机 二次加密
     *
     * @param inputPass
     * @return
     */
    public static String formPassFormPass(String inputPass) {
        String str = "" + getSalt.charAt(0) + getSalt.charAt(2) + inputPass + getSalt.charAt(4) + getSalt.charAt(6);
        return MD5(str);
    }

    /**
     * 第二次md5--反解密 用户登录验证 ---　salt　可随机
     *
     * @param formPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(4) + salt.charAt(6);
        return MD5(str);
    }

}
