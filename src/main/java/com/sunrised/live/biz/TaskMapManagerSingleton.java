package com.sunrised.live.biz;

import java.util.HashMap;
import java.util.Map;

public class TaskMapManagerSingleton {

    public static final int STATUS_ING=1;
    public static final int STATUS_FINISHED=2;
    public static final int STATUS_ERROR=3;

    private static TaskMapManagerSingleton instance;

    private TaskMapManagerSingleton() {

    }

    public static TaskMapManagerSingleton getInstance() {
        if (instance == null) {
            synchronized (TaskMapManagerSingleton.class) {
                if (instance == null) {
                    instance = new TaskMapManagerSingleton();
                }
            }
        }
        return instance;
    }

    //url 状态 0正在处理 -1 主动结束 -2 被动结束
    private Map<String, Integer> taskMap;

    private Map<String, Integer> getTaskMap() {
        if (taskMap == null) {
            synchronized (taskMap) {
                if (taskMap == null) {
                    taskMap = new HashMap<>();
                }
            }
        }
        return taskMap;
    }


    public void put(String key, Integer status) {
        getTaskMap().put(key, status);
    }

    public Integer get(String key) {
        return getTaskMap().get(key);
    }

    public void destroy(String key){
        getTaskMap().remove(key);
    }

}
