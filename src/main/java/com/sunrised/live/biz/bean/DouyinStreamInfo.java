package com.sunrised.live.biz.bean;

import lombok.Data;

/**
 * Created by tamas on 2020/3/26.
 */
@Data
public class DouyinStreamInfo {

    private String nickname;

    private String m3u8Url;

    private long finishSecond;

    public boolean isFinished() {
        if (finishSecond < (System.currentTimeMillis() / 1000) - 10) {
            return true;
        } else {
            return false;
        }
    }


}
