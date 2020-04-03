package com.sunrised.live.service;

import com.sunrised.live.model.Live;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MsgService {
    public String sendMsg(){
        return String.valueOf(System.currentTimeMillis())+"....................................................................................................................................................................................";
    }

    public String stopLiveEvent() {
        return "停止直播";
    }

    public List<Live> getLiveList() {
        List<Live> lives = new ArrayList<>();
        lives.add(new Live("www.baidu.com/"+UUID.randomUUID().toString(), "/tiktok/live/", UUID.randomUUID().toString()));
        lives.add(new Live("www.baidu.com/"+UUID.randomUUID().toString(), "/tiktok/live/", UUID.randomUUID().toString()));
        lives.add(new Live("www.baidu.com/"+UUID.randomUUID().toString(), "/tiktok/live/", UUID.randomUUID().toString()));
        lives.add(new Live("www.baidu.com/"+UUID.randomUUID().toString(), "/tiktok/live/", UUID.randomUUID().toString()));
        lives.add(new Live("www.baidu.com/"+UUID.randomUUID().toString(), "/tiktok/live/", UUID.randomUUID().toString()));
        lives.add(new Live("www.baidu.com/"+UUID.randomUUID().toString(), "/tiktok/live/", UUID.randomUUID().toString()));
        return lives;
    }
}
