package com.sunrised.live.util;

import com.google.gson.Gson;
import com.sunrised.live.LiveApplication;
import com.sunrised.live.biz.bean.MpResult;
import com.sunrised.live.biz.util.FileUtils;
import com.sunrised.live.biz.util.OkHttpKnife;
import com.sunrised.live.config.exception.AuthException;
import com.sunrised.live.model.RegisterResult;
import javafx.scene.control.Alert;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RegisterUtil {

    private static long timestamp = -1;

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

    public static RegisterResult doRegister() {

        if(timestamp != -1) {
            if(System.currentTimeMillis() - timestamp < 5*60*1000) {
                RegisterResult result = new RegisterResult();
                result.setStatus(RegisterResult.STATUS_OK);
                result.setMessage("5\u5206\u949f\u5185\u4e0d\u518d\u9a8c\u8bc1");
                return result;
            }
        }

        timestamp = System.currentTimeMillis();
        try {
            String key = RegisterUtil.readKeyFromFile();
            String serialNumber = RegisterUtil.getMixSerialNumber();
            final String url = "http://web-pro.raisedsun.com/pub/live-auth";
            Map<String, String> params = new HashMap<>();
            params.put("key", key);
            params.put("deviceId", serialNumber);
            MpResult result = OkHttpKnife.sendPost(url, null, params);
            RegisterResult registerResult = new Gson().fromJson(result.getBody(), RegisterResult.class);
            if(!(RegisterResult.STATUS_OK == registerResult.getStatus())) {
                throw new AuthException("token error");
            }
            return registerResult;
        } catch (AuthException e) {
            try {
                Runtime.getRuntime().exec("mshta vbscript:msgbox(\"\u6388\u6743\u7801\u9a8c\u8bc1\u5931\u8d25\",64,\"\u64cd\u4f5c\u63d0\u793a\")(window.close)");
            } catch (Exception re) {

            }
            RegisterResult result = new RegisterResult();
            result.setMessage("\u6388\u6743\u7801\u9a8c\u8bc1\u5931\u8d25\uff0c30\u79d2\u540e\u81ea\u52a8\u5173\u95ed\u7a0b\u5e8f");
            result.setStatus(RegisterResult.STATUS_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
