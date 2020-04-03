package com.sunrised.live.biz.service;

import com.sunrised.live.biz.TaskListener;
import com.sunrised.live.biz.util.LiveFFmpegUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by tamas on 2020/3/26.
 */
@Service
public class AsyncService {
    @Async
    public void downloadLiveStream(String m3u8Url, String savePath, String username, String saveName, TaskListener taskListener) {
        //String fileName = username + MyDateUtil.yyyymmddhhmmss(System.currentTimeMillis()) + ".mp4";
        String fileName = saveName + ".mp4";
        fileName = fileName.replace(" ", "");
        fileName = fileName.replace(":", "-");
        LiveFFmpegUtil.downloadLiveVideoStream(m3u8Url, savePath + File.separator + fileName);

    }
}
