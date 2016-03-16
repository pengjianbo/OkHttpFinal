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

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import cn.finalteam.toolsfinal.StringUtils;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Response;
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
    private List<Interceptor> networkInterceptorList;
    private List<Interceptor> interceptorList;
    private SSLSocketFactory sslSocketFactory;
    private Dispatcher dispatcher;

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
        this.proxy = builder.proxy;
        this.networkInterceptorList = builder.networkInterceptorList;
        this.interceptorList = builder.interceptorList;
        this.sslSocketFactory = builder.sslSocketFactory;
        this.dispatcher = builder.dispatcher;
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
        private List<Interceptor> networkInterceptorList;
        private List<Interceptor> interceptorList;
        private SSLSocketFactory sslSocketFactory;
        private Dispatcher dispatcher;

        public Builder() {
            certificateList = new ArrayList<>();
            followSslRedirects = true;
            followRedirects = true;
            retryOnConnectionFailure = true;
            networkInterceptorList = new ArrayList<>();
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
         * 设置缓存-并且添加网络拦截器修改响应头(有无网络都先读缓存)
         * 强制响应缓存者根据该值校验新鲜性.即与自身的Age值,与请求时间做比较.如果超出max-age值,则强制去服务器端验证.以确保返回一个新鲜的响应.
         * @param cache
         * @param cacheTime 缓存时间 单位秒
         * @return
         */
        public Builder setCacheAge(Cache cache, final int cacheTime) {
            setCache(cache, String.format("max-age=%d", cacheTime));

            return this;
        }

        /**
         * 设置缓存-并且添加网络拦截器修改响应头(有无网络都先读缓存)
         * 允许缓存者发送一个过期不超过指定秒数的陈旧的缓存.
         * @param cache
         * @param cacheTime 缓存时间 单位秒
         * @return
         */
        public Builder setCacheStale(Cache cache, final int cacheTime) {
            setCache(cache, String.format("max-stale=%d", cacheTime));
            return this;
        }

        /**
         * 设置缓存-并且添加网络拦截器修改响应头(有无网络都先读缓存)
         * @param cache
         * @param cacheControlValue Cache-Control值
         * @return
         */
        public Builder setCache(Cache cache, final String cacheControlValue) {
            Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", cacheControlValue)
                            .build();
                }
            };
            networkInterceptorList.add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
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

        /**
         * 设置网络拦截器
         * @param interceptors
         * @return
         */
        public Builder setNetworkInterceptors(List<Interceptor> interceptors) {
            if (interceptors != null) {
                networkInterceptorList.addAll(interceptors);
            }
            return this;
        }

        /**
         * 设置应用拦截器
         * @param interceptors
         * @return
         */
        public Builder setInterceptors(List<Interceptor> interceptors) {
            this.interceptorList = interceptors;
            return this;
        }

        /**
         * 设置SSLSocketFactory实例
         * @param sslSocketFactory
         * @return
         */
        public Builder setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        /**
         * 设置Dispatcher实例
         * @param dispatcher
         * @return
         */
        public Builder setDispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
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

    public List<Interceptor> getNetworkInterceptorList() {
        return networkInterceptorList;
    }

    public List<Interceptor> getInterceptorList() {
        return interceptorList;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }
}
