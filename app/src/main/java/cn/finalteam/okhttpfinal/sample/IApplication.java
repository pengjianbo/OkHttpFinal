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

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.finalteam.okhttpfinal.Part;
import okhttp3.Headers;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/10 下午3:07
 */
public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttpFinal();

        initGalleryFinal();
    }

    private void initOkHttpFinal() {

        List<Part> commomParams = new ArrayList<>();
        Headers commonHeaders = new Headers.Builder().build();

        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setCommenParams(commomParams)
                .setCommenHeaders(commonHeaders)
                .setTimeout(Constants.REQ_TIMEOUT)
                        //.setCookieJar(CookieJar.NO_COOKIES)
                        //.setCertificates(...)
                        //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
                .setDebug(true);
        OkHttpFinal.getInstance().init(builder.build());
    }


    private void initGalleryFinal() {
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
        .build();

        //配置imageloader
        ImageLoader imageloader = new UILImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, ThemeConfig.CYAN)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }
}
