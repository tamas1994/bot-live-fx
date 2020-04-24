package com.sunrised.live.biz.util;


import com.sunrised.live.biz.TaskListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by tamas on 2020/3/26.
 */
@Slf4j
public class LiveFFmpegUtil {

    public static void downloadLiveVideoStream(String taskKey, String m3u8Url, String outputPath, TaskListener taskListener) {
        String command = "ffmpeg -i " + m3u8Url + " -vcodec copy -acodec copy -absf aac_adtstoasc " + outputPath;
        log.info("command:"+command);
        CmdUtil.runCommandI(taskKey, command, taskListener);
    }


}
