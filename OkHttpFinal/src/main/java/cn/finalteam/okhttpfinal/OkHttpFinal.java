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
import com.squareup.okhttp.OkHttpClient;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
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

    private static OkHttpFinal mOkHttpFinal;

    private OkHttpFinal(Builder builder) {
        this.mCommonParamsMap = builder.mCommonParamsMap;
        this.mCommonHeaderMap = builder.mCommonHeaderMap;
        this.mCertificateList = builder.mCertificateList;
        this.mHostnameVerifier = builder.mHostnameVerifier;
        this.mTimeout = builder.mTimeout;
    }

    public void init() {
        this.mOkHttpClient = OkHttpFactory.getOkHttpClientFactory(mTimeout);
        if (mCertificateList != null && mCertificateList.size() > 0) {
            HttpsCerManager httpsCerManager = new HttpsCerManager(mOkHttpClient);
            httpsCerManager.setCertificates(mCertificateList);
        }

        mOkHttpFinal = this;
    }

    public static class Builder {
        private Map<String, String> mCommonParamsMap;
        private Map<String, String> mCommonHeaderMap;
        private List<InputStream> mCertificateList;
        private HostnameVerifier mHostnameVerifier;
        private long mTimeout;

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
            this.mCommonParamsMap = paramsMap;
            return this;
        }

        /**
         * 公共header
         * @param headerMap
         * @return
         */
        public Builder setCommenHeader(Map<String, String> headerMap) {
            this.mCommonHeaderMap = headerMap;
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
        okHttpFinal.init();
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
