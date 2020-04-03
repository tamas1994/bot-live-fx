package com.sunrised.live.biz.util;

import java.io.File;
import java.io.InputStream;

/**
 * Created by tamas on 2020/2/9.
 */
public class ToolUtil {

    private static final String TOOLS_RELATIVE_PATH = "tools";

    public static void enhanceTools() {
        enhanceFfmpeg(getFFmpegPath());
    }


    private static void enhanceFfmpeg(String ffmpegPath) {
        if (!new File(ffmpegPath).exists()) {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("tools/ffmpeg.exe");
            FileUtils.saveInputStream2File(inputStream, ffmpegPath);
        }
    }


    public static String getFFmpegPath() {
        String ffmpegPath = FileUtils.getPath(TOOLS_RELATIVE_PATH) + File.separator + "ffmpeg.exe";
        return ffmpegPath;
    }


}
