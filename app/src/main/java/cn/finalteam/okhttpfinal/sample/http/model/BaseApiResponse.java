package cn.finalteam.okhttpfinal.sample.http.model;

import cn.finalteam.okhttpfinal.ApiResponse;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/29 下午4:08
 */
public class BaseApiResponse extends ApiResponse {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
