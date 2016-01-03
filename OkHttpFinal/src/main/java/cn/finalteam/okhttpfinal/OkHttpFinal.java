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

import cn.finalteam.okhttpfinal.https.HttpsCerManager;
import cn.finalteam.toolsfinal.StringUtils;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import okhttp3.OkHttpClient;
import okio.Buffer;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/10 上午11:33
 */
public class OkHttpFinal {

    private OkHttpClient mOkHttpClient;

    private Map<String, String> mCommonParamsMap;
    private Map<String, String> mCommonHeaderMap;
    private List<InputStream> mCertificateList;
    private HostnameVerifier mHostnameVerifier;
    private long mTimeout;
    private boolean mDebug;

    private static OkHttpFinal mOkHttpFinal;

    private OkHttpFinal(Builder builder) {
        this.mCommonParamsMap = builder.mCommonParamsMap;
        this.mCommonHeaderMap = builder.mCommonHeaderMap;
        this.mCertificateList = builder.mCertificateList;
        this.mHostnameVerifier = builder.mHostnameVerifier;
        this.mTimeout = builder.mTimeout;
        this.mDebug = builder.mDebug;
    }

    public void init(int timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(mTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(mTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(mTimeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .followRedirects(true);
        if ( getHostnameVerifier() != null ) {
            builder.hostnameVerifier(mHostnameVerifier);
        }

        if (mCertificateList != null && mCertificateList.size() > 0) {
            HttpsCerManager httpsCerManager = new HttpsCerManager(builder);
            httpsCerManager.setCertificates(mCertificateList);
        }

        if ( timeout == -1 ) {
            long globalTimeout = mTimeout;
            //设置请求时间
            builder.connectTimeout(globalTimeout, TimeUnit.MILLISECONDS);
            builder.writeTimeout(globalTimeout, TimeUnit.MILLISECONDS);
            builder.readTimeout(globalTimeout, TimeUnit.MILLISECONDS);
        } else {
            //设置请求时间
            builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        }

        this.mOkHttpClient = builder.build();

        HttpRequest.setDebug(mDebug);
        mOkHttpFinal = this;
    }

    public static class Builder {
        private Map<String, String> mCommonParamsMap;
        private Map<String, String> mCommonHeaderMap;
        private List<InputStream> mCertificateList;
        private HostnameVerifier mHostnameVerifier;
        private long mTimeout;
        private boolean mDebug;

        public Builder() {
            this.mCommonParamsMap = new HashMap<>();
            this.mCommonHeaderMap = new HashMap<>();
            this.mCertificateList = new ArrayList<>();
        }

        /**
         * 添加公共参数
         * @param paramsMap
         * @return
         */
        public Builder setCommenParams(Map<String, String> paramsMap){
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                if ( !StringUtils.isEmpty(entry.getKey()) ) {
                    String value = "";
                    if ( !StringUtils.isEmpty(entry.getValue()) ) {
                        value = entry.getValue();
                    }
                    mCommonParamsMap.put(entry.getKey(), value);
                }
            }
            return this;
        }

        /**
         * 公共header
         * @param headerMap
         * @return
         */
        public Builder setCommenHeader(Map<String, String> headerMap) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                if ( !StringUtils.isEmpty(entry.getKey()) ) {
                    String value = "";
                    if ( !StringUtils.isEmpty(entry.getValue()) ) {
                        value = entry.getValue();
                    }
                    mCommonHeaderMap.put(entry.getKey(), value);
                }
            }
            return this;
        }

        /**
         * 指定证书
         * @param certificates
         * @return
         */
        public Builder setCertificates(InputStream... certificates) {
            for(InputStream inputStream:certificates) {
                if ( inputStream != null ) {
                    mCertificateList.add(inputStream);
                }
            }
            return this;
        }

        public Builder setCertificates(String... certificates) {
            for(String certificate:certificates) {
                if (!StringUtils.isEmpty(certificate)) {
                    mCertificateList.add(new Buffer()
                            .writeUtf8(certificate)
                            .inputStream());
                }
            }
            return this;
        }

        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.mHostnameVerifier = hostnameVerifier;
            return this;
        }

        /**
         * 设置调试开关
         * @param debug
         * @return
         */
        public Builder setDebug(boolean debug) {
            this.mDebug = debug;
            return this;
        }

        /**
         * 设置timeout
         * @param timeout
         * @return
         */
        public Builder setTimeout(long timeout) {
            this.mTimeout = timeout;
            return this;
        }

        public OkHttpFinal build() {
            return new OkHttpFinal(this);
        }
    }

    public static OkHttpFinal getOkHttpFinal() {
        if (mOkHttpFinal == null) {
            return getDefaultOkHttpFinal();
        }
        return mOkHttpFinal;
    }

    public static OkHttpFinal getDefaultOkHttpFinal() {
        OkHttpFinal okHttpFinal = new Builder().setTimeout(Constants.REQ_TIMEOUT).build();
        okHttpFinal.init(Constants.REQ_TIMEOUT);
        return okHttpFinal;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Map<String, String> getCommonParams() {
        return mCommonParamsMap;
    }

    public List<InputStream> getCertificateList() {
        return mCertificateList;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    public long getTimeout() {
        return mTimeout;
    }

    public Map<String, String> getCommonHeaderMap() {
        return mCommonHeaderMap;
    }
}
