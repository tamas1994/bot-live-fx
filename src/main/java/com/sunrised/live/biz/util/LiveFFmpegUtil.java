package com.sunrised.live.biz.util;


import com.sunrised.live.biz.TaskListener;

/**
 * Created by tamas on 2020/3/26.
 */
public class LiveFFmpegUtil {

    public static void downloadLiveVideoStream(String taskKey, String m3u8Url, String outputPath, TaskListener taskListener) {
        String command = "ffmpeg -i " + m3u8Url + " -vcodec copy -acodec copy -absf aac_adtstoasc " + outputPath;
        CmdUtil.runCommandI(taskKey, command, taskListener);
    }


}
