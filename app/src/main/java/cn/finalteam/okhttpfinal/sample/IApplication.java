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

package cn.finalteam.okhttpfinal.sample;

import android.app.Application;
import cn.finalteam.okhttpfinal.OkHttpFinal;
import java.util.HashMap;
import java.util.Map;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/10 下午3:07
 */
public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Map<String, String> commonParamMap = new HashMap<>();
        Map<String, String> commonHeaderMap = new HashMap<>();

        OkHttpFinal okHttpFinal = new OkHttpFinal.Builder()
                .setCommenParams(commonParamMap)
                .setCommenHeader(commonHeaderMap)
                .setTimeout(Constants.REQ_TIMEOUT)
                .setDebug(true)
                //.setCertificates(...)
                //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())

        .build();
        okHttpFinal.init(Constants.REQ_TIMEOUT);
    }
}
