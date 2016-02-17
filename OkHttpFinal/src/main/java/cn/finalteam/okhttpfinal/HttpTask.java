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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

import cn.finalteam.toolsfinal.JsonFormatUtils;
import cn.finalteam.toolsfinal.StringUtils;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Desction:Http请求Task
 * Author:pengjianbo
 * Date:15/7/3 上午11:14
 */
public class HttpTask extends AsyncTask<Void, Long, ResponseData> {

    public static final String DEFAULT_HTTP_TASK_KEY = "default_http_task_key";

    private String url;
    private RequestParams params;
    private BaseHttpRequestCallback callback;
    private Headers headers;
    private String requestKey;
    private Method method;
    private OkHttpClient okHttpClient;

    public HttpTask(Method method, String url, RequestParams params, BaseHttpRequestCallback callback) {
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

        okHttpClient = OkHttpFinal.getInstance().getOkHttpClient();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (params.headers != null) {
            headers = params.headers.build();
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
            String srcUrl = url;
            //构建请求Request实例
            Request.Builder builder = new Request.Builder();

            switch (method) {
                case GET:
                    url = Utils.getFullUrl(url, params.getUrlParams(), params.isUrlEncoder());
                    builder.get();
                    break;
                case DELETE:
                    url = Utils.getFullUrl(url, params.getUrlParams(), params.isUrlEncoder());
                    builder.delete();
                    break;
                case HEAD:
                    url = Utils.getFullUrl(url, params.getUrlParams(), params.isUrlEncoder());
                    builder.head();
                    break;
                case POST:
                    RequestBody body = params.getRequestBody();
                    if (body != null) {
                        builder.post(new ProgressRequestBody(body, this));
                    }
                    break;
                case PUT:
                    RequestBody bodyPut = params.getRequestBody();
                    if (bodyPut != null) {
                        builder.put(new ProgressRequestBody(bodyPut, this));
                    }
                    break;
                case PATCH:
                    RequestBody bodyPatch = params.getRequestBody();
                    if (bodyPatch != null) {
                        builder.put(new ProgressRequestBody(bodyPatch, this));
                    }
                    break;
            }
            builder.url(url).tag(srcUrl).headers(headers);
            Request request = builder.build();
            if (Constants.DEBUG) {
                ILogger.d("url=" + srcUrl + "?" + params.toString());
            }
            Call call = okHttpClient.newCall(request);
            OkHttpCallManager.getInstance().addCall(url, call);
            //执行请求
            response = call.execute();
        } catch (Exception e) {
            if (Constants.DEBUG) {
                ILogger.e("Exception=%s", e);
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

    protected void updateProgress(int progress, long networkSpeed, int done) {
        publishProgress((long)progress, networkSpeed, (long)done);
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        if (callback != null) {
            long progress = values[0];
            long networkSpeed = values[1];
            long done = values[2];
            callback.onProgress((int)progress, networkSpeed, done == 1L);
        }
    }

    @Override
    protected void onPostExecute(ResponseData responseData) {
        super.onPostExecute(responseData);

        OkHttpCallManager.getInstance().removeCall(url);
        //判断请求是否在这个集合中
        if (!HttpTaskHandler.getInstance().contains(requestKey)) {
            return;
        }

        if (callback != null) {
            callback.headers = responseData.getHeaders();
            callback.onResponse(responseData.getResponse(), responseData.getHeaders());
            callback.setResponseHeaders(responseData.getHeaders());
        }

        if (!responseData.isResponseNull()) {//请求得到响应
            if (responseData.isSuccess()) {//成功的请求
                String respBody = responseData.getResponse();
                if (Constants.DEBUG) {
                    ILogger.d("url=" + url + "\n result=" + JsonFormatUtils.formatJson(respBody));
                }
                parseResponseBody(responseData, callback);
            } else {//请求失败
                int code = responseData.getCode();
                String msg = responseData.getMessage();
                if (Constants.DEBUG) {
                    ILogger.d("url=" + url + "\n response failure code=" + code + " msg=" + msg);
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
                    ILogger.d("url=" + url + "\n response empty");
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
     * @param responseData 请求的response
     * @param callback 请求回调
     */
    private void parseResponseBody(ResponseData responseData, BaseHttpRequestCallback callback) {
        //回调为空，不向下执行
        if (callback == null) {
            return;
        }

        String result = responseData.getResponse();

        if (StringUtils.isEmpty(result)) {
            callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_NULL, "result empty");
            return;
        }

        if (callback.type == String.class) {
            callback.onSuccess(responseData.getHeaders(), result);
            callback.onSuccess(result);
            return;
        } else if ( callback.type == JSONObject.class) {
            JSONObject jsonObject = null;
            try {
                jsonObject = JSON.parseObject(result);
            } catch (Exception e) {
                ILogger.e(e);
            }
            if (jsonObject != null) {
                callback.onSuccess(responseData.getHeaders(), jsonObject);
                callback.onSuccess(jsonObject);
                return;
            }
        } else if (callback.type == JSONArray.class) {
            JSONArray jsonArray = null;
            try {
                jsonArray = JSON.parseArray(result);
            } catch (Exception e) {
                ILogger.e(e);
            }

            if (jsonArray != null) {
                callback.onSuccess(responseData.getHeaders(), jsonArray);
                callback.onSuccess(jsonArray);
                return;
            }
        } else {
            Object obj = null;
            try {
                obj = JSON.parseObject(result, callback.type);
            } catch (Exception e) {
                ILogger.e(e);
            }
            if (obj != null) {
                callback.onSuccess(responseData.getHeaders(), obj);
                callback.onSuccess(obj);
                return;
            }
        }
        //接口请求失败
        callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_JSON_EXCEPTION, "json exception");
    }

}
