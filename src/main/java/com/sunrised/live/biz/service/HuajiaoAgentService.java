package com.sunrised.live.biz.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sunrised.live.biz.bean.HuajiaoStreamInfo;
import com.sunrised.live.biz.bean.MpResult;
import com.sunrised.live.biz.util.OkHttpKnife;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tamas on 2020/3/28.
 */
@Slf4j
@Service
public class HuajiaoAgentService {

    private static final String HEADER_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";


    public HuajiaoStreamInfo getStreamInfoByUrl(String url) {

        String liveIdStarReg = "liveid=";
        String liveId = url.substring(url.indexOf(liveIdStarReg) + liveIdStarReg.length(), url.length());

        int endIndex = liveId.indexOf("&");
        if (endIndex == -1) {
            endIndex = liveId.length();
        }
        liveId = liveId.substring(0, endIndex);
        long nowMillis = System.currentTimeMillis();
        String streamInfoUrl = "http://h.huajiao.com/api/getFeedInfo?_rate=xd&stype=m3u8&sid=" + (nowMillis + 4) + ".8953&liveid=" + liveId + "&_=" + (nowMillis + 7) + "&callback=Zepto" + nowMillis;
        Map<String, String> headersMap = new HashMap<>();

        headersMap.put("Content-Type", "application/json; charset=utf-8");
        headersMap.put("User-Agent", HEADER_USER_AGENT);

        try {
            HuajiaoStreamInfo huajiaoStreamInfo = new HuajiaoStreamInfo();
            MpResult mpResult = OkHttpKnife.sendGet(streamInfoUrl, headersMap, null);

            String jsonp = mpResult.getBody();
            //log.info("jsonp:"+jsonp);
            String jsonStr = jsonp.substring(jsonp.indexOf("(") + 1, jsonp.lastIndexOf(");"));
            //log.info("jsonStr:" + jsonStr);
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonStr);
            JsonElement liveJsonEle = jsonObject.get("data").getAsJsonObject().get("live");
            if (liveJsonEle.isJsonNull()) {
                huajiaoStreamInfo.setFinished(true);
                return huajiaoStreamInfo;
            }
            String m3u8Url = liveJsonEle.getAsJsonObject().get("main").getAsString();
            String nickname = jsonObject.get("data").getAsJsonObject().get("feed").getAsJsonObject().get("author").getAsJsonObject().get("nickname").getAsString();

            huajiaoStreamInfo.setFinished(false);
            huajiaoStreamInfo.setM3u8Url(m3u8Url);
            huajiaoStreamInfo.setNickname(nickname);

            return huajiaoStreamInfo;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


}
