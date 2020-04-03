package com.sunrised.live.biz.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流
 *
 * @author shaojing
 */
@Slf4j
public class StreamGobbler extends Thread {

    private static final long MINUTE_MILLIS=60000L;

    public static final String TYPE_ERROR = "ERROR";
    public static final String TYPE_STDOUT = "STDOUT";

    InputStream is;
    String type;
    OutputStream os;

    StringBuilder errorBuilder;
    StringBuilder stdBuilder;

    OutputStream commandOs;
    long createMillis;

    boolean isShowOutput;

    public String getErrorInfo(){
        return errorBuilder.toString();
    }

    public String getStdInfo(){
        return stdBuilder.toString();
    }

    StreamGobbler(InputStream is, String type) {
        this(is, type, null,true);
    }

    StreamGobbler(InputStream is, String type, boolean isShowOutput) {
        this(is, type, null,isShowOutput);
    }

    StreamGobbler(InputStream is, OutputStream os, String type, boolean isShowOutput) {
        this(is, type, null,isShowOutput);
        this.commandOs=os;
        this.createMillis=System.currentTimeMillis();
    }

    StreamGobbler(InputStream is, String type, OutputStream redirect, boolean isShowOutput) {
        this.is = is;
        this.type = type;
        this.os = redirect;
        this.isShowOutput=isShowOutput;

        this.errorBuilder = new StringBuilder();
        this.stdBuilder = new StringBuilder();
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

                switch (type) {
                    case TYPE_ERROR:
                        if (isShowOutput){
                            errorBuilder.append(line);
                            log.info("error:"+line);
                        }
                        break;
                    case TYPE_STDOUT:
                        if (isShowOutput){
                            stdBuilder.append(line);
                            log.info("stdout:"+line);
                        }
                        break;
                }

                //log.info(Thread.currentThread().getName() + "---------" + type + ">" + line);



                if (this.createMillis>0 && commandOs!=null){
                    long now=System.currentTimeMillis();
                    if (now-createMillis>MINUTE_MILLIS*90){
                        String command="q";
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
