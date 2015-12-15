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
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;

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
    private Headers headers;
    private String requestKey;
    private Method method;
    private OkHttpClient okHttpClient;
    private OkHttpFinal okHttpFinal;

    public HttpTask(Method method, String url, RequestParams params, BaseHttpRequestCallback callback, long timeout) {
        this.method = method;
        this.url = url;
        this.params = params;
        this.callback = callback;
        if (params == null) {
            this.params = params = new RequestParams();
        }
        this.requestKey = params.getHttpTaskKey();
        if (StringUtils.isEmpty(requestKey)) {
            requestKey = DEFAULT_HTTP_TASK_KEY;
        }

        //将请求的URL及参数组合成一个唯一请求
        HttpTaskHandler.getInstance().addTask(this.requestKey, this);

        okHttpFinal = OkHttpFinal.getOkHttpFinal();
        okHttpClient = okHttpFinal.getOkHttpClient();
        HostnameVerifier hostnameVerifier = okHttpFinal.getHostnameVerifier();
        if (hostnameVerifier != null) {
            okHttpClient.setHostnameVerifier(hostnameVerifier);
        }

        if ( timeout == -1 ) {
            long globalTimeout = okHttpFinal.getTimeout();
            //设置请求时间
            okHttpClient.setConnectTimeout(globalTimeout, TimeUnit.MILLISECONDS);
            okHttpClient.setWriteTimeout(globalTimeout, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(globalTimeout, TimeUnit.MILLISECONDS);
        } else {
            //设置请求时间
            okHttpClient.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
            okHttpClient.setWriteTimeout(timeout, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(timeout, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (params.headerMap != null) {
            headers = Headers.of(params.headerMap);
        }

        if (callback != null) {
            callback.onStart();
        }
    }

    @Override
    protected ResponseData doInBackground(Void... voids) {
        Response response = null;
        ResponseData responseData = new ResponseData();
        try {
            //OkHttpClient client = OkHttpFactory.getOkHttpClientFactory(timeout);
            String srcUrl = url;
            //构建请求Request实例
            Request.Builder builder = new Request.Builder();

            switch (method) {
                case GET:
                    url = Utils.getFullUrl(url, params.getUrlParams());
                    builder.get();
                    break;
                case DELETE:
                    url = Utils.getFullUrl(url, params.getUrlParams());
                    builder.delete();
                    break;
                case HEAD:
                    url = Utils.getFullUrl(url, params.getUrlParams());
                    builder.head();
                    break;
                case POST:
                    RequestBody body = params.getRequestBody();
                    if (body != null) {
                        builder.post(new ProgressRequestBody(body, callback));
                    }
                    break;
                case PUT:
                    RequestBody bodyPut = params.getRequestBody();
                    if (bodyPut != null) {
                        builder.put(new ProgressRequestBody(bodyPut, callback));
                    }
                    break;

                case PATCH:
                    RequestBody bodyPatch = params.getRequestBody();
                    if (bodyPatch != null) {
                        builder.put(new ProgressRequestBody(bodyPatch, callback));
                    }
                    break;
            }

            builder.url(url).tag(srcUrl).headers(headers);
            Request request = builder.build();
            if (Constants.DEBUG) {
                Logger.d("url=" + url + "?" + params.toString());
            }
            //执行请求
            response = okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            if (Constants.DEBUG) {
                Logger.e("Exception=", e);
            }
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
                    Logger.d("url=" + url + "\n result=" + JsonFormatUtils.formatJson(respBody));
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
     *
     * @param result 请求的response 内容
     * @param callback 请求回调
     */
    private void parseResponseBody(String result, BaseHttpRequestCallback callback) {
        //回调为空，不向下执行
        if (callback == null) {
            return;
        }

        if (StringUtils.isEmpty(result)) {
            callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_NULL, "result empty");
            return;
        }

        if (callback.mType == String.class) {
            callback.onSuccess(result);
            return;
        } else if ( callback.mType == JSONObject.class) {
            try {
                callback.onSuccess(JSON.parseObject(result));
                return;
            } catch (Exception e) {
                Logger.e(e);
            }
        } else {

            try {
                Object obj = JSON.parseObject(result, callback.mType);
                //Object obj = gson.fromJson(result, callback.mType);
                if (obj != null) {
                    callback.onSuccess(obj);
                    return;
                }
            } catch (Exception e) {
                Logger.e(e);
            }
        }
        //接口请求失败
        callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_JSON_EXCEPTION, "json exception");
    }

}
