package com.sunrised.live.biz.service;


import com.sunrised.live.biz.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by tamas on 2020/3/26.
 */
@Service
public class LiveProcessService {

    @Autowired
    AsyncService asyncService;

    @Autowired
    DouyinAgentService douyinAgentService;

    @Autowired
    HuajiaoAgentService huajiaoAgentService;

    @Autowired
    KuaishouAgentService kuaishouAgentService;

    /**
     * 处理抖音视频流
     *
     * @param livePageUrl
     * @param savePath
     * @return
     */
    public Result processDouyin(String livePageUrl, String savePath, String saveName) {

        if (!this.isSavePathOk(savePath)) {
            return Result.fail("视频保存路径不存在，请重新设置");
        }


        DouyinLiveInfo douyinLiveInfo = douyinAgentService.getDouyinLiveInfo(livePageUrl);
        if (douyinLiveInfo == null) {
            return Result.fail("获取抖音直播信息失败");
        }
        DouyinStreamInfo douyinStreamInfo = douyinAgentService.getDouyinStreamInfo(douyinLiveInfo);
        if (douyinStreamInfo == null) {
            return Result.fail("获取抖音直播流失败");
        }

        //TODO 判断直播是否结束
        asyncService.downloadLiveStream(douyinStreamInfo.getM3u8Url(), savePath, douyinStreamInfo.getNickname(), saveName);
        return Result.success("添加抖音用户" + douyinStreamInfo.getNickname() + "的直播流下载任务成功");
    }


    /**
     * 处理快手视频流
     *
     * @param livePageUrl
     * @param savePath
     * @return
     */
    public Result processKuaishou(String livePageUrl, String savePath, String saveName) {

        if (!this.isSavePathOk(savePath)) {
            return Result.fail("视频保存路径不存在，请重新设置");
        }

        KuaishouStreamInfo kuaishouStreamInfo = kuaishouAgentService.getKuaishouStreamInfo(livePageUrl);

        if (kuaishouStreamInfo == null) {
            return Result.fail("获取快手直播流失败");
        }

        if (kuaishouStreamInfo.isFinished()) {
            return Result.fail("该快手直播已结束，请换一个链接试试");
        }

        String m3u8Url = kuaishouStreamInfo.getM3u8Url();
        if (m3u8Url.contains("?")) {
            String[] strArr = m3u8Url.split("\\?");
            m3u8Url = strArr[0];
        }
        asyncService.downloadLiveStream(m3u8Url, savePath, kuaishouStreamInfo.getUserName(), saveName);
        return Result.success("添加快手用户" + kuaishouStreamInfo.getUserName() + "的直播流下载任务成功");
    }

    /**
     * 处理花椒视频流
     *
     * @param livePageUrl
     * @param savePath
     * @return
     */
    public Result processHuajiao(String livePageUrl, String savePath, String saveName) {

        if (!this.isSavePathOk(savePath)) {
            return Result.fail("视频保存路径不存在，请重新设置");
        }

        HuajiaoStreamInfo huajiaoStreamInfo = huajiaoAgentService.getStreamInfoByUrl(livePageUrl);

        if (huajiaoStreamInfo == null) {
            return Result.fail("获取花椒直播流失败");
        }

        if (huajiaoStreamInfo.isFinished()) {
            return Result.fail("该花椒直播已结束，请换一个链接试试");
        }

        String m3u8Url = huajiaoStreamInfo.getM3u8Url();
        if (m3u8Url.contains("?")) {
            String[] strArr = m3u8Url.split("\\?");
            m3u8Url = strArr[0];
        }
        asyncService.downloadLiveStream(m3u8Url, savePath, huajiaoStreamInfo.getNickname(), saveName);
        return Result.success("添加花椒用户" + huajiaoStreamInfo.getNickname() + "的直播流下载任务成功");
    }

    private boolean isSavePathOk(String savePath) {
        File file = new File(savePath);
        if (file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }


}