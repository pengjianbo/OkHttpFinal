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

package cn.finalteam.okhttpfinal.dm.v2;

import cn.aigestudio.downloader.interfaces.IDListener;
import java.io.File;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午11:16
 */
public class DownloadListener {

    public void onPrepare() {

    }

    public void onStart(String fileName, String realUrl, int fileLength) {

    }

    public void onProgress(int progress) {

    }

    public void onStop(int progress) {

    }

    public void onFinish(File file) {

    }

    public void onError(int status, String error) {

    }

}
