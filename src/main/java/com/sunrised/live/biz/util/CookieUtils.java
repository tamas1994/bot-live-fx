package com.sunrised.live.biz.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tamas on 2019/10/8.
 */
public class CookieUtils {

    public static Map<String, String> parseCookies(List<String> cookieList) {

        HashMap result = new HashMap();

        if (cookieList == null || cookieList.size() == 0) {
            return result;
        }


        for (String item : cookieList) {
            if (MyStringUtils.isEmpty(item)) {
                continue;
            }

            int start = item.indexOf("=");
            int end = item.indexOf(";");

            String key = item.substring(0, start);
            String value = item.substring(start + 1, end);
            result.put(key, value);

        }
        return result;

    }


    public static String cookieMap2Str(Map<String, String> cookieMap) {

        StringBuilder cookieBuilder = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            if ("EXPIRED".equals(entry.getValue())) {
                continue;
            }
            if (index > 0) {
                cookieBuilder.append("; ");
            }
            cookieBuilder.append(entry.getKey() + "=" + entry.getValue());
            index++;
        }
        return cookieBuilder.toString();
    }

    public static Map<String, String> str2CookieMap(String cookieStr) {

        Map<String, String> cookieMap = new HashMap<>();
        if (MyStringUtils.isEmpty(cookieStr)) {
            return cookieMap;
        }

        String[] cookieArr = cookieStr.split("; ");

        for (int i = 0; i < cookieArr.length; i++) {
            String item = cookieArr[i];

            int index = item.indexOf("=");

            String key = item.substring(0, index);
            String value = item.substring(index + 1, item.length());

            cookieMap.put(key, value);

        }

        return cookieMap;

    }

    public static void main(String[] args) {

        String cookieStr = "login_certificate=LUE60hawnF6B39oJfNcKzRcSvuXh3r894jVRLZ+2UVY=; ticket=dfc6bbef74c92113d59a132785ef96b0b34d5a4f; login_sid_ticket=5ab0124c4201b151664136885797469b35e9af8a; cert=SxEmG5I_sbVxUb1CWvi65dSi9NNknHKz; ticket_certificate=P32mwTtv6329f6FiWFRoSk1de2SJnuxpKSMarenghpE=; ticket_id=gh_a3decc175f2c; uuid=ffb0539bbbfa714456a6a65796893cdc; fake_id=3868218998; ticket_uin=3868218998; bizuin=3868218998";

        str2CookieMap(cookieStr);


    }


}
