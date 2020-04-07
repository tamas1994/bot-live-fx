package com.sunrised.live.biz.service;


import com.sunrised.live.biz.TaskListener;
import com.sunrised.live.biz.TaskMapManagerSingleton;
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


    private static final String PREFIX_DOUYIN = "https://v.douyin.com";
    private static final String PREFIX_KUAISHOU = "https://v.kuaishou.com";
    private static final String PREFIX_HUAJIAO = "http://h.huajiao.com";
    private static final String PREFIX_HUAJIAO2 = "https://h.huajiao.com";

    public String sendStopTaskRequest(String livePageUrl) {
        Integer status = TaskMapManagerSingleton.getInstance().get(livePageUrl);
        if (status == null) {
            return "任务不存在";
        }
        if (status == TaskMapManagerSingleton.STATUS_FINISHED) {
            return "该任务已经是结束状态";
        }
        if (status == TaskMapManagerSingleton.STATUS_ING) {
            TaskMapManagerSingleton.getInstance().put(livePageUrl, TaskMapManagerSingleton.STATUS_FINISHED);
            return "发送结束任务请求成功，稍等几秒后任务会自动结束";
        }
        return "发生了其他异常";

    }

    public void startTask(String livePageUrl, String path, String saveName, TaskListener taskListener) {

        String filePath = path + File.separator + saveName + ".mp4";
        File file = new File(filePath);
        if (file.exists()) {
            Result.fail("文件已存在，重命名");
        }
        TaskMapManagerSingleton.getInstance().put(livePageUrl, TaskMapManagerSingleton.STATUS_ING);
        if (livePageUrl.startsWith(PREFIX_DOUYIN)) {
            this.processDouyin(livePageUrl, path, saveName, taskListener);
        } else if (livePageUrl.startsWith(PREFIX_KUAISHOU)) {
            this.processKuaishou(livePageUrl, path, saveName, taskListener);
        } else if (livePageUrl.startsWith(PREFIX_HUAJIAO) || livePageUrl.startsWith(PREFIX_HUAJIAO2)) {
            this.processHuajiao(livePageUrl, path, saveName, taskListener);
        } else {
            TaskMapManagerSingleton.getInstance().put(livePageUrl, TaskMapManagerSingleton.STATUS_ERROR);
            Result.fail("只支持抖音、快手、花椒直播页面");
            taskListener.onAddError("只支持抖音、快手、花椒直播页面");
        }
    }

    /**
     * 处理抖音视频流
     *
     * @param livePageUrl
     * @param savePath
     * @return
     */
    private Result processDouyin(String livePageUrl, String savePath, String saveName, TaskListener taskListener) {

        if (!this.isSavePathOk(savePath)) {
            String errorMessage = "视频保存路径不存在，请重新设置";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }


        DouyinLiveInfo douyinLiveInfo = douyinAgentService.getDouyinLiveInfo(livePageUrl);
        if (douyinLiveInfo == null) {
            String errorMessage = "获取抖音直播信息失败";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }
        DouyinStreamInfo douyinStreamInfo = douyinAgentService.getDouyinStreamInfo(douyinLiveInfo);
        if (douyinStreamInfo == null) {
            String errorMessage = "获取抖音直播流失败";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }

        //TODO 判断直播是否结束
        asyncService.downloadLiveStream(livePageUrl, douyinStreamInfo.getM3u8Url(), savePath, douyinStreamInfo.getNickname(), saveName, taskListener);
        return Result.success("添加抖音用户" + douyinStreamInfo.getNickname() + "的直播流下载任务成功");
    }


    /**
     * 处理快手视频流
     *
     * @param livePageUrl
     * @param savePath
     * @return
     */
    private Result processKuaishou(String livePageUrl, String savePath, String saveName, TaskListener taskListener) {

        if (!this.isSavePathOk(savePath)) {
            String errorMessage = "视频保存路径不存在，请重新设置";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }

        KuaishouStreamInfo kuaishouStreamInfo = kuaishouAgentService.getKuaishouStreamInfo(livePageUrl);

        if (kuaishouStreamInfo == null) {
            String errorMessage = "获取快手直播流失败";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }

        if (kuaishouStreamInfo.isFinished()) {
            String errorMessage = "该快手直播已结束，请换一个链接试试";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }

        String m3u8Url = kuaishouStreamInfo.getM3u8Url();
        if (m3u8Url.contains("?")) {
            String[] strArr = m3u8Url.split("\\?");
            m3u8Url = strArr[0];
        }
        asyncService.downloadLiveStream(livePageUrl, m3u8Url, savePath, kuaishouStreamInfo.getUserName(), saveName, taskListener);
        return Result.success("添加快手用户" + kuaishouStreamInfo.getUserName() + "的直播流下载任务成功");
    }

    /**
     * 处理花椒视频流
     *
     * @param livePageUrl
     * @param savePath
     * @return
     */
    private Result processHuajiao(String livePageUrl, String savePath, String saveName, TaskListener taskListener) {

        if (!this.isSavePathOk(savePath)) {
            String errorMessage = "视频保存路径不存在，请重新设置";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }

        HuajiaoStreamInfo huajiaoStreamInfo = huajiaoAgentService.getStreamInfoByUrl(livePageUrl);

        if (huajiaoStreamInfo == null) {
            String errorMessage = "获取花椒直播流失败";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }

        if (huajiaoStreamInfo.isFinished()) {
            String errorMessage = "该花椒直播已结束，请换一个链接试试";
            taskListener.onAddError(errorMessage);
            return Result.fail(errorMessage);
        }

        String m3u8Url = huajiaoStreamInfo.getM3u8Url();
        if (m3u8Url.contains("?")) {
            String[] strArr = m3u8Url.split("\\?");
            m3u8Url = strArr[0];
        }
        asyncService.downloadLiveStream(livePageUrl, m3u8Url, savePath, huajiaoStreamInfo.getNickname(), saveName, taskListener);
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