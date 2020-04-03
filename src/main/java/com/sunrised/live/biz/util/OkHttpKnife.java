package com.sunrised.live.biz.util;

import com.sunrised.live.biz.bean.MpResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by tamas on 2019/9/18.
 */
@Slf4j
public class OkHttpKnife {


    private static final OkHttpClient CLIENT = new OkHttpClient();

    private static final String ENCODE = "utf-8";


    public static MpResult sendGet(String url, Map<String, String> headers, Map<String, String> params) {

        Request.Builder requestBuilder = new Request.Builder()
                .get()
                .url(url);
        requestBuilderSetupHeaders(requestBuilder, headers);

        Request request = requestBuilder.build();
        try (Response response = CLIENT.newCall(request).execute()) {
            MpResult mpResult = new MpResult();
            mpResult.setBody(response.body().string());
            mpResult.setCookieMap(CookieUtils.parseCookies(response.headers("Set-Cookie")));
            return mpResult;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String map2Params(Map<String,String> paramsMap){
        StringBuilder paramBuilder =new StringBuilder();
        if (paramsMap != null && paramsMap.size() > 0) {
            Set<String> paramsKeySet = paramsMap.keySet();
            int index=0;
            for (String key : paramsKeySet) {
                if (index>0){
                    paramBuilder.append("&");
                }
                String value = paramsMap.get(key);
                paramBuilder.append(key + "=" + urlEncode(value));
                index++;
            }
        }
        return paramBuilder.toString();
    }

    public static String getImage(String url, Map<String, String> headers, Map<String, String> params) {

        Request.Builder requestBuilder = new Request.Builder()
                .get()
                .url(url);
        requestBuilderSetupHeaders(requestBuilder, headers);

        Request request = requestBuilder.build();
        try (Response response = CLIENT.newCall(request).execute()) {
            byte[] data = response.body().bytes();
            //FileUtil.saveBytesToFile(data,"/Users/tamas/Desktop","a.jpeg");
            String base64 = new String(Base64.encodeBase64(data));
            return "data:image/jpg;base64," + base64;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static MpResult sendPost(String url, Map<String, String> headers, Map<String, String> params) {

        RequestBody requestBody = FormBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=" + ENCODE)
                , map2Params(params));


        Request.Builder requestBuilder = new Request.Builder()
                .post(requestBody)
                .url(url);

        requestBuilderSetupHeaders(requestBuilder, headers);

        Request request = requestBuilder.build();

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.2", 8888));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //builder.proxy(proxy);
        OkHttpClient client = builder.build();

        try (Response response = client.newCall(request).execute()) {
            MpResult mpResult = new MpResult();
            mpResult.setBody(response.body().string());
            Map<String,String> setCookieMap=CookieUtils.parseCookies(response.headers("Set-Cookie"));
            mpResult.setCookieMap(setCookieMap);
            return mpResult;
        } catch (IOException e) {
            return null;
        }

    }


    public static MpResult postData(String url, Map<String, String> headers, byte[] buf) {

        RequestBody requestBody = MultipartBody.create(MediaType.parse("application/octet-stream")
                , buf);
        Request.Builder requestBuilder = new Request.Builder()
                .post(requestBody)
                .url(url);

        requestBuilderSetupHeaders(requestBuilder, headers);

        Request request = requestBuilder.build();

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.2", 8888));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //builder.proxy(proxy);
        OkHttpClient client = builder.build();

        try (Response response = client.newCall(request).execute()) {
            MpResult mpResult = new MpResult();
            mpResult.setBody(response.body().string());
            mpResult.setCookieMap(CookieUtils.parseCookies(response.headers("Set-Cookie")));
            return mpResult;
        } catch (IOException e) {
            return null;
        }

    }

    public static String convertMap2Cookie(Map<String, String> map) {
        StringBuilder cookieBuilder = new StringBuilder();

        Set<String> headerKeySet = map.keySet();
        int i = 0;
        for (String key : headerKeySet) {
            String value = map.get(key);
            if (i > 0) {
                cookieBuilder.append("; ");
            }
            cookieBuilder.append(key + "=" + value);
            i++;
        }
        return cookieBuilder.toString();
    }

    private static void requestBuilderSetupHeaders(Request.Builder requestBuilder, Map<String, String> headers) {
        if (headers != null) {
            Set<String> headerKeySet = headers.keySet();
            for (String key : headerKeySet) {
                String value = headers.get(key);
                requestBuilder.addHeader(key, value);
            }
        }
    }


    /**
     * urlencode
     *
     * @param inStr
     * @return
     */
    public static String urlEncode(String inStr) {
        if (MyStringUtils.isEmpty(inStr)){
            return "";
        }
        String outStr = null;
        try {
            outStr = URLEncoder.encode(inStr, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            outStr = "";
        }

        return outStr;
    }


    public static boolean saveVideo(String url, String filePath, String fileName, Map<String, String> headers, Map<String, String> params) {
        Request.Builder requestBuilder = new Request.Builder()
                .get()
                .url(url);
        requestBuilderSetupHeaders(requestBuilder, headers);

        Request request = requestBuilder.build();
        try (Response response = CLIENT.newCall(request).execute()) {
            byte[] data = response.body().bytes();
            FileUtils.saveBytesToFile(data, filePath, fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
