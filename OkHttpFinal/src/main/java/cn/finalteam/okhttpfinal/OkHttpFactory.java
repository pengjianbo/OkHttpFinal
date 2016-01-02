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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Desction:生产http client
 * Author:pengjianbo
 * Date:15/7/3 下午6:13
 */
public class OkHttpFactory {

    public static OkHttpClient getOkHttpClientFactory(long timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置请求时间
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        //请求不重复
        builder.retryOnConnectionFailure(false);
        //请求支持重定向
        builder.followRedirects(true);
        //启用cookie
        builder.cookieJar(new SimpleCookieJar());

        return builder.build();
    }

    private static class SimpleCookieJar implements CookieJar {
        private final List<Cookie> cookies = new ArrayList<>();

        @Override
        public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            this.cookies.addAll(cookies);
        }

        @Override
        public synchronized List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> result = new ArrayList<>();
            for (Cookie cookie : cookies) {
                if (cookie.matches(url)) {
                    result.add(cookie);
                }
            }
            return result;
        }
    }
}
