/*
 * Copyright (C) 2015 彭建波(pengjianbo@finalteam.cn), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.finalteam.okhttpfinal;

import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;

import cn.finalteam.toolsfinal.StringUtils;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okio.Buffer;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/1/28 0028 14:33
 */
public class OkHttpFinalConfiguration {

    private List<Part> commonParams;
    protected Headers commonHeaders;
    private List<InputStream> certificateList;
    private HostnameVerifier hostnameVerifier;
    private long timeout = Constants.REQ_TIMEOUT;
    private boolean debug;
    private CookieJar cookieJar;
    private Cache cache;
    private Authenticator authenticator;
    private CertificatePinner certificatePinner;
    private boolean followSslRedirects;
    private boolean followRedirects;
    private boolean retryOnConnectionFailure;
    private Proxy proxy;

    private OkHttpFinalConfiguration(final Builder builder) {
        this.commonParams = builder.commonParams;
        this.commonHeaders = builder.commonHeaders;
        this.certificateList = builder.certificateList;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.timeout = builder.timeout;
        this.debug = builder.debug;
        this.cookieJar = builder.cookieJar;
        this.cache = builder.cache;
        this.authenticator = builder.authenticator;
        this.certificatePinner = builder.certificatePinner;
        this.followSslRedirects = builder.followSslRedirects;
        this.followRedirects = builder.followRedirects;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
    }

    public static class Builder {
        private List<Part> commonParams;
        private Headers commonHeaders;
        private List<InputStream> certificateList;
        private HostnameVerifier hostnameVerifier;
        private long timeout;
        private boolean debug;
        private CookieJar cookieJar = CookieJar.NO_COOKIES;
        private Cache cache;
        private Authenticator authenticator;
        private CertificatePinner certificatePinner;
        private boolean followSslRedirects;
        private boolean followRedirects;
        private boolean retryOnConnectionFailure;
        private Proxy proxy;

        public Builder() {
            this.certificateList = new ArrayList<>();
            followSslRedirects = true;
            followRedirects = true;
            retryOnConnectionFailure = true;
        }

        /**
         * 添加公共参数
         * @param params
         * @return
         */
        public Builder setCommenParams(List<Part> params){
            this.commonParams = params;
            return this;
        }

        /**
         * 公共header
         * @param headers
         * @return
         */
        public Builder setCommenHeaders(Headers headers) {
            commonHeaders = headers;
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
                    certificateList.add(inputStream);
                }
            }
            return this;
        }

        public Builder setCertificates(String... certificates) {
            for(String certificate:certificates) {
                if (!StringUtils.isEmpty(certificate)) {
                    certificateList.add(new Buffer()
                            .writeUtf8(certificate)
                            .inputStream());
                }
            }
            return this;
        }

        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        public Builder setCertificatePinner(CertificatePinner certificatePinner) {
            this.certificatePinner = certificatePinner;
            return this;
        }

        /**
         * 设置调试开关
         * @param debug
         * @return
         */
        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        /**
         * 设置timeout
         * @param timeout
         * @return
         */
        public Builder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 设置cookie jar
         * @param cookieJar
         * @return
         */
        public Builder setCookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
            return this;
        }

        /**
         * 设置缓存
         * @param cache
         * @return
         */
        public Builder setCache(Cache cache) {
            this.cache = cache;
            return this;
        }

        /**
         * 设置Authenticator
         * @param authenticator
         * @return
         */
        public Builder setAuthenticator(Authenticator authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        public Builder setFollowSslRedirects(boolean followProtocolRedirects) {
            this.followSslRedirects = followProtocolRedirects;
            return this;
        }

        public Builder setFollowRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        public Builder setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public Builder setProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public OkHttpFinalConfiguration build() {
            return new OkHttpFinalConfiguration(this);
        }
    }

    public List<Part> getCommonParams() {
        return commonParams;
    }

    public Headers getCommonHeaders() {
        return commonHeaders;
    }

    public List<InputStream> getCertificateList() {
        return certificateList;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public long getTimeout() {
        return timeout;
    }

    public boolean isDebug() {
        return debug;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public Cache getCache() {
        return cache;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public CertificatePinner getCertificatePinner() {
        return certificatePinner;
    }

    public boolean isFollowSslRedirects() {
        return followSslRedirects;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public Proxy getProxy() {
        return proxy;
    }
}
