package com.sunrised.live.biz.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sunrised.live.biz.bean.DouyinLiveInfo;
import com.sunrised.live.biz.bean.DouyinStreamInfo;
import com.sunrised.live.biz.bean.HuoshanStreamInfo;
import com.sunrised.live.biz.bean.MpResult;
import com.sunrised.live.biz.util.OkHttpKnife;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tamas on 2020/3/25.
 */
@Slf4j
@Service
public class HuoshanAgentService {
    private static final String HEADER_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";


    public HuoshanStreamInfo getHuoshanStreamInfo(String url) {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json; charset=utf-8");
        headersMap.put("User-Agent", HEADER_USER_AGENT);
        headersMap.put("Origin", "https://reflow.huoshan.com/");

        try {
            MpResult mpResult = OkHttpKnife.sendGet(url, headersMap, null);
            String html = mpResult.getBody();
            //log.info("html：" + html);

            String startReg="var room = ";
            String jsonStr=html.substring(html.indexOf(startReg)+startReg.length(),html.length());
            //log.info("jsonStr1:"+jsonStr);

            jsonStr=jsonStr.substring(0,jsonStr.indexOf(",\n"));

            log.info("jsonStr2:"+jsonStr);

            JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonStr);

            String m3u8Url=jsonObject.get("stream_url").getAsJsonObject().get("hls_pull_url").getAsString();
            String nickname=jsonObject.get("owner").getAsJsonObject().get("nickname").getAsString();

            HuoshanStreamInfo huoshanStreamInfo=new HuoshanStreamInfo();
            huoshanStreamInfo.setM3u8Url(m3u8Url);
            huoshanStreamInfo.setNickname(nickname);
            log.info("m3u8Url:"+m3u8Url);
            log.info("nickname:"+nickname);
            return huoshanStreamInfo;

        } catch (Exception e) {
            log.error("获取直播视频流信息失败");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){

        HuoshanAgentService huoshanAgentService=new HuoshanAgentService();


        huoshanAgentService.getHuoshanStreamInfo("https://reflow.huoshan.com/hotsoon/s/jNnCDabkS78/ ");


    }


}
