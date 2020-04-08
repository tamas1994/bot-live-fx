package com.sunrised.live.util;

import java.security.MessageDigest;

/**
 * Created by aaron on 2017/6/5.
 */

public class HashUtil {

    private static byte[] encode2bytes(String source) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(source.getBytes("UTF-8"));
            result = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 计算字符串md5值
     *
     * @param source 待加密字符串
     * @return 加密结果
     */
    public static String md5(String source) {

        byte[] data = encode2bytes(source);
        StringBuilder hexString = new StringBuilder();
        for (byte b : data) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String bcrypt(String source) {


        return "";
    }


}
