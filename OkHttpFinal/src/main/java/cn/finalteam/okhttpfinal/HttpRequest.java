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

    public static void get(String url, RequestParams params, BaseHttpRequestCallback callback) {
        get(url, params, callback, Constants.REQ_TIMEOUT);
    }

    public static void get(String url, RequestParams params, BaseHttpRequestCallback callback, int timeOut) {
        executeRequest("GET", url, params, callback, timeOut);
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
        executeRequest("POST", url, params, callback, timeOut);
    }

    private static void executeRequest(String method, String url, RequestParams params, BaseHttpRequestCallback callback, int timeout) {
        HttpTask task = new HttpTask(method, url, params, callback, timeout);
        task.execute();
    }

}
