package com.sunrised.live.biz.service;

import com.sunrised.live.biz.TaskListener;
import com.sunrised.live.biz.TaskMapManagerSingleton;
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
    public void downloadLiveStream(String taskKey, String m3u8Url, String savePath, String username, String saveName, TaskListener taskListener) {
        //String fileName = username + MyDateUtil.yyyymmddhhmmss(System.currentTimeMillis()) + ".mp4";
        for (int i = 0; i < 50; i++) {
            String fileName = saveName + "-" + i + ".mp4";
            fileName = fileName.replace(" ", "");
            fileName = fileName.replace(":", "-");
            LiveFFmpegUtil.downloadLiveVideoStream(taskKey, m3u8Url, savePath + File.separator + fileName, taskListener);
            Integer status = TaskMapManagerSingleton.getInstance().get(taskKey);
            if (status != TaskMapManagerSingleton.STATUS_ING) {
                break;
            }
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        taskListener.onTaskStop();
        TaskMapManagerSingleton.getInstance().put(taskKey, TaskMapManagerSingleton.STATUS_FINISHED);
    }
}
