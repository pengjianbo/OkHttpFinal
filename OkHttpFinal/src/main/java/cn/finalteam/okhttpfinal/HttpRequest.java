/*
 * Copyright (C) 2015 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.okhttpfinal;

import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import java.io.File;

/**
 * Desction:http请求类
 * Author:pengjianbo
 * Date:15/9/22 下午10:17
 */
public class HttpRequest {

    public static void setDebug(boolean debug) {
        Constants.DEBUG = debug;
        Logger.init("OkHttpFinal", debug);
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

    /**
     * Get请求 使用全局timeout
     * @param url
     * @param params
     * @param callback
     */
    public static void get(String url, RequestParams params, BaseHttpRequestCallback callback) {
        get(url, params, callback, -1);
    }

    public static void get(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest(Method.GET, url, params, callback, timeOut);
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

    /**
     * Post请求 使用全局timeout
     * @param url
     * @param params
     * @param callback
     */
    public static void post(String url, RequestParams params, BaseHttpRequestCallback callback) {
        post(url, params, callback, -1);
    }

    public static void post(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest(Method.POST, url, params, callback, timeOut);
    }

    public static void put(String url) {
        put(url, null, null);
    }

    public static void put(String url, RequestParams params) {
        put(url, params, null);
    }

    public static void put(String url, BaseHttpRequestCallback callback) {
        put(url, null, callback);
    }

    /**
     * put请求 使用全局timeout
     * @param url
     * @param params
     * @param callback
     */
    public static void put(String url, RequestParams params, BaseHttpRequestCallback callback) {
        put(url, params, callback, -1);
    }

    public static void put(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest(Method.PUT, url, params, callback, timeOut);
    }

    public static void delete(String url) {
        delete(url, null, null);
    }

    public static void delete(String url, RequestParams params) {
        delete(url, params, null);
    }

    public static void delete(String url, BaseHttpRequestCallback callback) {
        delete(url, null, callback);
    }

    /**
     * delete请求 使用全局timeout
     * @param url
     * @param params
     * @param callback
     */
    public static void delete(String url, RequestParams params, BaseHttpRequestCallback callback) {
        delete(url, params, callback, -1);
    }

    public static void delete(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest(Method.DELETE, url, params, callback, timeOut);
    }

    public static void head(String url) {
        head(url, null, null);
    }

    public static void head(String url, RequestParams params) {
        head(url, params, null);
    }

    public static void head(String url, BaseHttpRequestCallback callback) {
        head(url, null, callback);
    }

    /**
     * head请求 使用全局timeout
     * @param url
     * @param params
     * @param callback
     */
    public static void head(String url, RequestParams params, BaseHttpRequestCallback callback) {
        head(url, params, callback, -1);
    }

    public static void head(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest(Method.HEAD, url, params, callback, timeOut);
    }

    public static void patch(String url) {
        patch(url, null, null);
    }

    public static void patch(String url, RequestParams params) {
        patch(url, params, null);
    }

    public static void patch(String url, BaseHttpRequestCallback callback) {
        patch(url, null, callback);
    }

    /**
     * patch请求 使用全局timeout
     * @param url
     * @param params
     * @param callback
     */
    public static void patch(String url, RequestParams params, BaseHttpRequestCallback callback) {
        patch(url, params, callback, -1);
    }

    public static void patch(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest(Method.PATCH, url, params, callback, timeOut);
    }

    /**
     * 取消请求
     * @param url
     */
    public static void cancel(String url) {
        if ( !StringUtils.isEmpty(url) ) {
            try {
                OkHttpFinal.getOkHttpFinal().getOkHttpClient().cancel(url);
            } catch (Exception e){
                Logger.e(e);
            }
        }
    }

    /**
     * 下载文件
     * @param url
     * @param target 保存的文件
     * @param callback
     */
    public static void download(String url, File target, FileDownloadCallback callback) {
        if (!StringUtils.isEmpty(url) && target != null) {
            FileDownloadTask task = new FileDownloadTask(url, target, callback);
            task.execute();
        }
    }

    private static void executeRequest(Method method, String url, RequestParams params, BaseHttpRequestCallback callback, int timeout) {
        if (!StringUtils.isEmpty(url)) {
            HttpTask task = new HttpTask(method, url, params, callback, timeout);
            task.execute();
        }
    }

}
