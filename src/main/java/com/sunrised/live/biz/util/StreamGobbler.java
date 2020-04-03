package com.sunrised.live.biz.util;

import com.sunrised.live.biz.TaskListener;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流
 *
 * @author shaojing
 */
@Slf4j
public class StreamGobbler extends Thread {

    private static final long MINUTE_MILLIS = 60000L;

    public static final String TYPE_ERROR = "ERROR";
    public static final String TYPE_STDOUT = "STDOUT";

    InputStream is;
    String type;
    OutputStream os;

    TaskListener taskListener;

    OutputStream commandOs;
    long createMillis;


    StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }

    StreamGobbler(InputStream is, OutputStream os, String type, TaskListener taskListener) {
        this(is, type, null);
        this.commandOs = os;
        this.createMillis = System.currentTimeMillis();
        this.taskListener = taskListener;
    }

    StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;

    }

    @Override
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            if (os != null) {
                pw = new PrintWriter(os);
            }
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null) {
                    pw.println(line);
                }

                //System.out.println("line:"+line);

                taskListener.onSendMsg(line);

                if (this.createMillis > 0 && commandOs != null) {
                    long now = System.currentTimeMillis();
                    if (now - createMillis > MINUTE_MILLIS * 90) {
                        String command = "q";
                        commandOs.write(command.getBytes());
                        commandOs.flush();
                    }
                }
            }
            if (pw != null) {
                pw.flush();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            log.info("清理流");
        }
    }
}
