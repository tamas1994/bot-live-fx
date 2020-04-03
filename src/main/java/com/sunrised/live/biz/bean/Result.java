package com.sunrised.live.biz.bean;


import lombok.Data;

/**
 * Created by tamas on 2018/6/10.
 */

@Data
public class Result {

    /**
     * 默认成功状态
     */
    private static final int STATUS_SUCCESS = 1;
    /**
     * 默认失败状态
     */
    private static final int STATUS_FAIL = -1;

    /**
     * 默认成功消息
     */
    private static final String DEF_MSG_SUCCESS = "成功";
    /**
     * 默认失败消息
     */
    private static final String DEF_MSG_FAIL = "失败";

    private int status;
    private String message;

    /**
     * 简单失败
     *
     * @return
     */

    public static Result fail() {
        return fail(STATUS_FAIL, DEF_MSG_FAIL);
    }

    public static Result fail(String message) {
        return fail(STATUS_FAIL, message);
    }

    public static Result fail(int status, String message) {
        Result result = new Result();
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }

    /**
     * 简单成功
     *
     * @return
     */
    public static Result success() {
        return success(STATUS_SUCCESS, DEF_MSG_SUCCESS);
    }

    public static Result success(String message) {
        return success(STATUS_SUCCESS, message);
    }

    public static Result success(int status, String message) {
        Result result = new Result();
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }


    public static WithData successWithData(Object data) {
        return successWithData(STATUS_SUCCESS, data);
    }
    /*
    public static WithData success(String message,Object data){

    }
    */

    public static WithData successWithData(int status, Object data) {
        WithData resultWithData = new WithData();
        resultWithData.setStatus(status);
        resultWithData.setMessage(DEF_MSG_SUCCESS);
        resultWithData.setData(data);
        return resultWithData;
    }






    @Data
    public static class WithData extends Result {
        Object data;
    }

}
