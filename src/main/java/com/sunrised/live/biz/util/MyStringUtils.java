package com.sunrised.live.biz.util;

import java.util.UUID;

/**
 * Created by tamas on 2018/6/10.
 */
public class MyStringUtils {
    public static boolean notEmpty(String str) {
        if (str == null) {
            return false;
        }
        if ("".equals(str)) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        return !notEmpty(str);
    }

    public static String buildUUID() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }


    public static String getFrontStr(String inStr){
        if (MyStringUtils.isEmpty(inStr)){
            return "";
        }
        if (inStr.length()<6){
            return inStr;
        }
        return inStr.substring(0,6)+"...";
    }

    public static boolean isVideoUrlOk(String url){
        if (MyStringUtils.isEmpty(url)){
            return false;
        }
        boolean condition1 = url.startsWith("https://mp.weixin.qq.com/s") || url.startsWith("http://mp.weixin.qq.com/s");
        boolean condition2 = url.startsWith("https://v.qq.com");
        boolean condition3 = url.startsWith("https://haokan.baidu.com/v") || url.startsWith("http://haokan.baidu.com/v");
        boolean condition4 = url.startsWith("https://mparticle.uc.cn") || url.startsWith("http://mparticle.uc.cn");
        if (!condition1 && !condition2 & !condition3 && !condition4) {
            return false;
        }
        return true;
    }

}
