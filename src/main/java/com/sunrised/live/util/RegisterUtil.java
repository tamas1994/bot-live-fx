package com.sunrised.live.util;

import com.google.gson.Gson;
import com.sunrised.live.biz.bean.MpResult;
import com.sunrised.live.biz.util.FileUtils;
import com.sunrised.live.biz.util.OkHttpKnife;
import com.sunrised.live.model.RegisterResult;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RegisterUtil {

    public static String getCPUSerialNumber() throws IOException {

        Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});

        process.getOutputStream().close();

        Scanner sc = new Scanner(process.getInputStream());

        String property = sc.next();

        String serial = sc.next();

        return serial;

    }

    public static String getHardDiskSerialNumber() throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec(new String[]{"wmic", "path", "win32_physicalmedia", "get", "serialnumber"});

        process.getOutputStream().close();

        Scanner sc = new Scanner(process.getInputStream());

        String property = sc.next();

        String serial = sc.next();

        return serial;

    }

    public static String getMixSerialNumber() {
        try {
            String cpuSerialNumber = getCPUSerialNumber();
            String diskSerialNumber = getHardDiskSerialNumber();
            StringBuffer mixSerialNumberBuffer = new StringBuffer();
            return HashUtil.md5(mixSerialNumberBuffer.append(cpuSerialNumber).append(diskSerialNumber.replace("_", "").replace(".", "")).toString());
        } catch (Exception e) {
            return "";
        }
    }

    private static final String KEY_FILE_NAME = "keyfile.txt";

    public static void saveKeyToFile(String key) {
        FileUtils.saveBytesToFile(key.getBytes(), "tools", KEY_FILE_NAME);
    }

    public static String readKeyFromFile() {
        return FileUtils.readTextFromFile("tools" + File.separator + KEY_FILE_NAME);
    }

    public static RegisterResult doRegister(String key, String serialNumber) {
        try {
            final String url = "http://web-pro.raisedsun.com/pub/live-auth";
            Map<String, String> params = new HashMap<>();
            params.put("key", key);
            params.put("deviceId", serialNumber);
            MpResult result = OkHttpKnife.sendPost(url, null, params);
            RegisterResult registerResult = new Gson().fromJson(result.getBody(), RegisterResult.class);
            return registerResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}