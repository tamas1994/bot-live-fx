package com.sunrised.live.biz;

public interface TaskListener {
    /**
     * 任务结束时回调此方法
     */
    void onTaskStop();

    /**
     * 任务内容输出时回调此方法
     *
     * @param msg
     */
    void onSendMsg(String msg);

    void onAddError(String errMsg);
}
