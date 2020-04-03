package com.sunrised.live.biz.service;

import com.sunrised.live.biz.bean.KuaishouStreamInfo;
import com.sunrised.live.biz.bean.MpResult;
import com.sunrised.live.biz.util.MyStringUtils;
import com.sunrised.live.biz.util.OkHttpKnife;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tamas on 2020/3/25.
 */
@Slf4j
@Service
public class KuaishouAgentService {
    private static final String HEADER_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";


    public KuaishouStreamInfo getKuaishouStreamInfo(String url) {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json; charset=utf-8");
        headersMap.put("User-Agent", HEADER_USER_AGENT);

        try {
            KuaishouStreamInfo kuaishouStreamInfo = new KuaishouStreamInfo();
            MpResult mpResult = OkHttpKnife.sendGet(url, headersMap, null);
            //log.info("快手网页访问结果:" + mpResult.getBody());
            Document document = Jsoup.parse(mpResult.getBody());
            String m3u8Url = document.select("video").attr("src");
            String username = document.select(".author-name").text();
            if (!MyStringUtils.isEmpty(m3u8Url)) {
                kuaishouStreamInfo.setM3u8Url(m3u8Url);
            } else {
                kuaishouStreamInfo.setFinished(true);
            }
            if (!MyStringUtils.isEmpty(username)) {
                kuaishouStreamInfo.setUserName(username);
            }
            return kuaishouStreamInfo;
        } catch (Exception e) {
            log.error("获取抖音直播信息失败");
            e.printStackTrace();
        }
        return null;

    }

}
