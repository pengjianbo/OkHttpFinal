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

import android.os.AsyncTask;
import android.text.TextUtils;
import cn.finalteam.toolsfinal.JsonFormatUtils;
import cn.finalteam.toolsfinal.JsonValidator;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;

/**
 * Desction:Http请求Task
 * Author:pengjianbo
 * Date:15/7/3 上午11:14
 */
public class HttpTask extends AsyncTask<Void, Void, ResponseData> {

    public static final String DEFAULT_HTTP_TASK_KEY = "default_http_task_key";

    private String url;
    private RequestParams params;
    private BaseHttpRequestCallback callback;
    private int timeout;
    private Headers headers;
    private String requestKey;
    private String method;

    public HttpTask(String method, String url, RequestParams params, BaseHttpRequestCallback callback, int timeout) {
        this.method = method;
        this.url = url;
        this.params = params;
        this.callback = callback;
        this.timeout = timeout;

        this.requestKey = params.getHttpTaskKey();
        if ( StringUtils.isEmpty(requestKey) ) {
            requestKey = DEFAULT_HTTP_TASK_KEY;
        }

        //将请求的URL及参数组合成一个唯一请求
        HttpTaskHandler.getInstance().addTask(this.requestKey, this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if ( params.headerMap != null ) {
            headers = Headers.of(params.headerMap);
        }

        if ( callback != null ) {
            callback.onStart();
        }
    }

    @Override
    protected ResponseData doInBackground(Void... voids) {
        OkHttpClient client = OkHttpFactory.getOkHttpClientFactory(timeout);
        ResponseData responseData = new ResponseData();

        //构建请求Request实例
        Request.Builder builder = new Request.Builder();
        builder.url(url).headers(headers);
        if (TextUtils.equals(method, "POST")) {
            RequestBody body = params.getRequestBody();
            builder.post(body);
        } else {
            Map<String, String> paramsMap = params.getUrlParams();
            StringBuffer urlFull = new StringBuffer();
            urlFull.append(url);
            if ( urlFull.indexOf("?", 0) < 0 && paramsMap.size() > 0) {
                urlFull.append("?");
            }
            Iterator<Map.Entry<String, String>> paramsIterator = paramsMap.entrySet().iterator();
            while (paramsIterator.hasNext()){
                Map.Entry<String, String> entry = paramsIterator.next();
                String key = entry.getKey();
                String value = entry.getValue();

                urlFull.append(key).append("=").append(value);
                if ( paramsIterator.hasNext() ) {
                    urlFull.append("&");
                }
            }
            url = urlFull.toString();
            builder.get();
        }
        Request request = builder.build();
        if (Constants.DEBUG) {
            Logger.d("url=" + url + "?" + params.toString());
        }
        //执行请求
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            if (e instanceof SocketTimeoutException) {
                responseData.setTimeout(true);
            } else if (e instanceof InterruptedIOException && TextUtils.equals(e.getMessage(),
                    "timeout")) {
                responseData.setTimeout(true);
            }
        }

        //获取请求结果
        if (response != null) {
            responseData.setResponseNull(false);
            responseData.setCode(response.code());
            responseData.setMessage(response.message());
            responseData.setSuccess(response.isSuccessful());
            String respBody = "";
            try {
                respBody = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            responseData.setResponse(respBody);
            responseData.setHeaders(response.headers());

        } else {
            responseData.setResponseNull(true);
        }
        return responseData;
    }

    @Override
    protected void onPostExecute(ResponseData responseData) {
        super.onPostExecute(responseData);

        if (!HttpTaskHandler.getInstance().contains(requestKey)) {
            return;
        }

        //判断请求是否在这个集合中
        if (!responseData.isResponseNull()) {//请求得到响应
            if (responseData.isSuccess()) {//成功的请求
                String respBody = responseData.getResponse();
                if (Constants.DEBUG) {
                    Logger.d("url=" + url +  "\n result=" + JsonFormatUtils.formatJson(respBody));
                }
                parseResponseBody(respBody, callback);
            } else {//请求失败
                int code = responseData.getCode();
                String msg = responseData.getMessage();
                if (Constants.DEBUG) {
                    Logger.d("url=" + url + "\n response failure code=" + code + " msg=" + msg);
                }
                if (code == 504) {
                    if (callback != null) {
                        callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_TIMEOUT,
                                "network error time out");
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(code, msg);
                    }
                }
            }
        } else {//请求无响应
            if (responseData.isTimeout()) {
                if (callback != null) {
                    callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_TIMEOUT,
                            "network error time out");
                }
            } else {
                if (Constants.DEBUG) {
                    Logger.d("url=" + url + "\n response empty");
                }
                if (callback != null) {
                    callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_UNKNOWN, "http exception");
                }
            }
        }

        if (callback != null) {
            callback.onFinish();
        }
    }

    /**
     * 解析响应数据
     * @param result 请求的response 内容
     * @param callback 请求回调
     */
    private void parseResponseBody(String result, BaseHttpRequestCallback callback) {

        //回调为空，不向下执行
        if(callback == null){
            return;
        }

        if (StringUtils.isEmpty(result) || !new JsonValidator().validate(result)) {
            callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_NULL, "result empty");
            return;
        }
        ApiResponse response = null;
        try {
            Gson gson = new Gson();
            Object obj = gson.fromJson(result, callback.getModelClazz());
            if (obj instanceof ApiResponse) {
                response = (ApiResponse) obj;
            } else {
                response = gson.fromJson(result, ApiResponse.class);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        if (response != null) {
            //默认超时
            callback.onSuccess(response);
            return;
        }

        //接口请求失败
        callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_JSON_EXCEPTION, "json exception");
        return;
    }
}
