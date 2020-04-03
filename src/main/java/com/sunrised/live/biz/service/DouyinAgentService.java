package com.sunrised.live.biz.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sunrised.live.biz.bean.DouyinLiveInfo;
import com.sunrised.live.biz.bean.DouyinStreamInfo;
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
public class DouyinAgentService {
    private static final String HEADER_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";


    public DouyinLiveInfo getDouyinLiveInfo(String url) {
        Map<String, String> headersMap = new HashMap<>();

        headersMap.put("Content-Type", "application/json; charset=utf-8");
        headersMap.put("User-Agent", HEADER_USER_AGENT);


        try {
            MpResult mpResult = OkHttpKnife.sendGet(url, headersMap, null);
            DouyinLiveInfo douyinLiveInfo = new DouyinLiveInfo();
            String html = mpResult.getBody();
            String roomIdStartReg = "var roomId = \"";
            String roomId = html.substring(html.indexOf(roomIdStartReg) + roomIdStartReg.length(), html.length());
            roomId = roomId.substring(0, roomId.indexOf("\""));
            String uidStartReg = "uid: \"";
            String uid = html.substring(html.indexOf(uidStartReg) + uidStartReg.length(), html.length());
            uid = uid.substring(0, uid.indexOf("\""));
            douyinLiveInfo.setUserId(uid);
            douyinLiveInfo.setRoomId(roomId);
            //log.info("douyinLiveInfo:" + new Gson().toJson(douyinLiveInfo));
            return douyinLiveInfo;
        } catch (Exception e) {
            log.error("获取抖音直播信息失败");
            e.printStackTrace();
        }
        return null;

    }

    public DouyinStreamInfo getDouyinStreamInfo(DouyinLiveInfo liveInfo) {
        String url = "https://webcast-hl.amemv.com/webcast/room/reflow/info/?room_id=" + liveInfo.getRoomId() + "&type_id=0&user_id=" + liveInfo.getUserId() + "&live_id=1&app_id=1128";
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json; charset=utf-8");
        headersMap.put("User-Agent", HEADER_USER_AGENT);
        headersMap.put("Origin", "https://www.iesdouyin.com");
        String referer = "https://www.iesdouyin.com/share/live/" + liveInfo.getRoomId() + "?u_code=m74dhi65&utm_campaign=client_share&app=aweme&utm_medium=ios&tt_from=copy&utm_source=copy";
        headersMap.put("Referer", referer);

        try {
            MpResult mpResult = OkHttpKnife.sendGet(url, headersMap, null);
            String jsonStr = mpResult.getBody();
            log.info("视频流信息：" + jsonStr);
            DouyinStreamInfo streamInfo = new DouyinStreamInfo();
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonStr);
            String nickname = jsonObject.get("data").getAsJsonObject().get("user").getAsJsonObject().get("nickname").getAsString();
            String m3u8Url = jsonObject.get("data").getAsJsonObject().get("room").getAsJsonObject().get("stream_url").getAsJsonObject().get("hls_pull_url").getAsString();
            long finishSecond = jsonObject.get("data").getAsJsonObject().get("room").getAsJsonObject().get("finish_time").getAsLong();
            streamInfo.setNickname(nickname);
            streamInfo.setM3u8Url(m3u8Url);
            streamInfo.setFinishSecond(finishSecond);
            return streamInfo;
        } catch (Exception e) {
            log.error("获取直播视频流信息失败");
            e.printStackTrace();
        }
        return null;

    }


}
