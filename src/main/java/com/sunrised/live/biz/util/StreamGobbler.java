package com.sunrised.live.biz.util;

import com.sunrised.live.biz.TaskListener;
import com.sunrised.live.biz.TaskMapManagerSingleton;
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


    InputStream is;
    OutputStream os;

    TaskListener taskListener;

    OutputStream commandOs;
    long createMillis;

    String taskKey;


    StreamGobbler(InputStream is, String taskKey) {
        this(is, taskKey, null);
    }

    StreamGobbler(InputStream is, OutputStream os, String type, TaskListener taskListener) {
        this(is, type, null);
        this.commandOs = os;
        this.createMillis = System.currentTimeMillis();
        this.taskListener = taskListener;
    }

    StreamGobbler(InputStream is, String taskKey, OutputStream redirect) {
        this.is = is;
        this.taskKey = taskKey;
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

                Integer status = TaskMapManagerSingleton.getInstance().get(taskKey);
                boolean finishCondition1 = status != null && status == TaskMapManagerSingleton.STATUS_FINISHED;
                boolean finishCondition2 = System.currentTimeMillis() - createMillis > MINUTE_MILLIS * 90;
                //上述两个条件满足任意一个就结束
                if (commandOs != null && (finishCondition1 || finishCondition2)) {
                    String command = "q";
                    commandOs.write(command.getBytes());
                    commandOs.flush();
                    taskListener.onTaskStop();
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
        TaskMapManagerSingleton.getInstance().put(taskKey,TaskMapManagerSingleton.STATUS_FINISHED);
        taskListener.onTaskStop();
    }
}
