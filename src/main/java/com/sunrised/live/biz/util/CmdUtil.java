package com.sunrised.live.biz.util;

import com.sunrised.live.biz.TaskListener;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tamas on 2019/11/7.
 */
@Slf4j
public class CmdUtil {

    /**
     * 运行shell命令
     *
     * @param command
     * @return
     */
    public static String runCommand(String command) {
        command = prehandleCommand(command);
        log.info("执行命令---------------：" + command);
        try {
            Process p = Runtime.getRuntime().exec(command);
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            p.waitFor();
            if (p.exitValue() != 0) {
                log.error("执行命令异常退出");
            }
            StringBuilder resultBuilder = new StringBuilder();
            String s = null;
            while ((s = reader.readLine()) != null) {
                resultBuilder.append(s);
            }
            return resultBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String runCommandI(String command, TaskListener taskListener) {
        try {
            if (isWindows()) {
                command = prehandleCommand(command);
            }

            //log.info("command:"+command );

            Process p = Runtime.getRuntime().exec(command);


            boolean isShowOutput = true;
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), p.getOutputStream(), StreamGobbler.TYPE_ERROR, taskListener);
            // kick off stderr
            errorGobbler.start();
            StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), StreamGobbler.TYPE_STDOUT);
            // kick off stdout
            outGobbler.start();
            p.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 执行交互式命令
     *
     * @param commands
     * @return
     */
    public static String runCommandI(String[] commands) {

        try {
            if (isWindows()) {
                for (int i = 0; i < commands.length; i++) {
                    commands[i] = prehandleCommand(commands[i]);
                }
            }
            Process p = Runtime.getRuntime().exec(commands);

            boolean isShowOutput = true;
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), StreamGobbler.TYPE_ERROR);
            // kick off stderr
            errorGobbler.start();
            StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), StreamGobbler.TYPE_STDOUT);
            // kick off stdout
            outGobbler.start();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";


    }

    private static String prehandleCommand(String command) {

        if (!isWindows()) {
            return command;
        }
        command = command.replace("ffmpeg", ToolUtil.getFFmpegPath());
        return command;
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return true;
        } else {
            return false;
        }
    }


}
