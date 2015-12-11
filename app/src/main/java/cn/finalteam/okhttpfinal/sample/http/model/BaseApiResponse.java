package cn.finalteam.okhttpfinal.sample.http.model;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/29 下午4:08
 */
public class BaseApiResponse<T> {
    private int code;
    private String msg;
    private T data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
