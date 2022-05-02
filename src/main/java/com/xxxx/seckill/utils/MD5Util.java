package com.xxxx.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * MD5 工具类
 * @author yangWu
 * @date 2022/3/30 4:22 PM
 * @version 1.0
 */
public class MD5Util {

    private static final String salt = "1a2b3c4d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    // 第一次加密 前端->后端
    public static String inputPassToFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    // 第二次加密 后端->数据库
    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    // 最终调用方法
    public static String inputPassToDBPass(String inputPass, String salt) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9", salt));
        System.out.println(inputPassToDBPass("123456", salt));
    }

}
