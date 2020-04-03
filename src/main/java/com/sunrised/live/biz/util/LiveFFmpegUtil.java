package com.sunrised.live.biz.util;


/**
 * Created by tamas on 2020/3/26.
 */
public class LiveFFmpegUtil {

    public static void downloadLiveVideoStream(String m3u8Url, String outputPath) {
        String command = "ffmpeg -i " + m3u8Url + " -vcodec copy -acodec copy -absf aac_adtstoasc " + outputPath;
        CmdUtil.runCommandI(command);
    }


}
