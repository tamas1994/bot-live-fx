package com.sunrised.live.biz.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by tamas on 2019/9/18.
 */
@Getter
@Setter
public class MpResult {
    Map<String, String> cookieMap;
    String body;
}
