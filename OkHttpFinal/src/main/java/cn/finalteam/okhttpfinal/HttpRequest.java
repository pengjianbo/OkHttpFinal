package cn.finalteam.okhttpfinal;

/**
 * Desction:http请求类
 * Author:pengjianbo
 * Date:15/9/22 下午10:17
 */
public class HttpRequest {

    public static void setDebug(boolean debug) {
        Constants.DEBUG = debug;
    }

    public static void get(String url) {
        get(url, null, null);
    }

    public static void get(String url, RequestParams params) {
        get(url, params, null);
    }

    public static void get(String url, BaseHttpRequestCallback callback) {
        get(url, null, callback);
    }

    public static void get(String url, RequestParams params, BaseHttpRequestCallback callback) {
        get(url, params, callback, Constants.REQ_TIMEOUT);
    }

    public static void get(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest(url, params, callback, timeOut);
    }

    public static void post(String url) {
        post(url, null, null);
    }

    public static void post(String url, RequestParams params) {
        post(url, params, null);
    }

    public static void post(String url, BaseHttpRequestCallback callback) {
        post(url, null, callback);
    }

    public static void post(String url, RequestParams params, BaseHttpRequestCallback callback) {
        post(url, params, callback, Constants.REQ_TIMEOUT);
    }

    public static void post(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest(url, params, callback, timeOut);
    }

    private static void executeRequest(String url, RequestParams params, BaseHttpRequestCallback callback, int timeout) {
        HttpTask task = new HttpTask(url, params, callback, timeout);
        task.execute();
    }

}
