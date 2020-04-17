package com.sunrised.live.model;

import lombok.Data;

@Data
public class RegisterResult {

    public static int STATUS_OK = 1;
    public static int STATUS_ERROR = -1;

    private int status;
    private String message;

}
