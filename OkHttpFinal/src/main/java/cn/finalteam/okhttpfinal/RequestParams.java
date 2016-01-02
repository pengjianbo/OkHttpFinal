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

import android.text.TextUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desction:Http请求参数类
 * Author:pengjianbo
 * Date:15/7/3 上午11:05
 */
public class RequestParams {

    protected ConcurrentHashMap<String, String> headerMap;

    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, FileWrapper> fileParams;

    protected HttpCycleContext httpCycleContext;
    private String httpTaskKey;
    private JSONObject jsonBody;
    private RequestBody requestBody;

    public RequestParams() {
        this(null);
    }

    public RequestParams(HttpCycleContext cycleContext) {
        this.httpCycleContext = cycleContext;
        init();
    }

    private void init() {
        headerMap = new ConcurrentHashMap<>();
        urlParams = new ConcurrentHashMap<>();
        fileParams = new ConcurrentHashMap<>();

        headerMap.put("charset", "UTF-8");


        //添加公共参数
        Map<String, String> commonParams = OkHttpFinal.getOkHttpFinal().getCommonParams();
        if ( commonParams != null && commonParams.size() > 0 ) {
            urlParams.putAll(commonParams);
        }

        //添加公共header
        Map<String, String> commonHeader = OkHttpFinal.getOkHttpFinal().getCommonHeaderMap();
        if ( commonHeader != null && commonHeader.size() > 0 ) {
            headerMap.putAll(commonHeader);
        }

        if ( httpCycleContext != null ) {
            httpTaskKey = httpCycleContext.getHttpTaskKey();
        }
    }

    public String getHttpTaskKey() {
        return this.httpTaskKey;
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if ( value == null ) {
            value = "";
        }

        if (!StringUtils.isEmpty(key)) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, int value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, float value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, double value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, boolean value) {
        put(key, String.valueOf(value));
    }

    /**
     * @param key
     * @param file
     */
    public void put(String key, File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }

        boolean isPng = file.getName().lastIndexOf("png") > 0 || file.getName().lastIndexOf("PNG") > 0;
        if (isPng) {
            put(key, file, "image/png; charset=UTF-8");
        }

        boolean isJpg = file.getName().lastIndexOf("jpg") > 0 || file.getName().lastIndexOf("JPG") > 0;
        if (isJpg) {
            put(key, file, "image/jpeg; charset=UTF-8");
        }

        if (!isPng && !isJpg) {
            put(key, new FileWrapper(file, null));
        }
    }

    public void put(String key, File file, String contentType) {
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }

        MediaType mediaType = null;
        try {
            mediaType = MediaType.parse(contentType);
        } catch (Exception e){
            Logger.e(e);
        }

        put(key, new FileWrapper(file, mediaType));
    }

    public void put(String key, File file, MediaType mediaType) {
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }

        put(key, new FileWrapper(file, mediaType));
    }

    public void put(String key, FileWrapper fileWrapper) {
        if (!StringUtils.isEmpty(key) && fileWrapper != null) {
            fileParams.put(key, fileWrapper);
        }
    }

    public void putAll(Map<String, String> params) {
        if ( params != null && params.size() > 0 ) {
            urlParams.putAll(params);
        }
    }

    public void putHeader(String key, String value) {
        if ( value == null ) {
            value = "";
        }

        if (!TextUtils.isEmpty(key)) {
            headerMap.put(key, value);
        }
    }

    public void putHeader(String key, int value) {
        putHeader(key, String.valueOf(value));
    }

    public void putHeader(String key, float value) {
        putHeader(key, String.valueOf(value));
    }

    public void putHeader(String key, double value) {
        putHeader(key, String.valueOf(value));
    }

    public void putHeader(String key, boolean value) {
        putHeader(key, String.valueOf(value));
    }

    public void clearMap() {
        urlParams.clear();
        fileParams.clear();
    }

    public void setJSONObject(JSONObject jsonBody) {
        this.jsonBody = jsonBody;
    }

    public void setCustomRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getUrlParams() {
        return urlParams;
    }

    protected RequestBody getRequestBody() {
        RequestBody body = null;
        if (jsonBody != null) {
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody.toJSONString());
        } else if (requestBody != null) {
            body = requestBody;
        } else if (fileParams.size() > 0) {
            boolean hasData = false;
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
                hasData = true;
            }

            for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
                FileWrapper file = entry.getValue();
                if (file != null) {
                    hasData = true;
                    builder.addFormDataPart(entry.getKey(), file.getFileName(), RequestBody.create(file.getMediaType(), file.getFile()));
                }
            }
            if (hasData) {
                body = builder.build();
            }
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            boolean hasData = false;
            for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
                hasData = true;
            }
            if (hasData) {
                body = builder.build();
            }
        }

        return body;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        return result.toString();
    }
}
