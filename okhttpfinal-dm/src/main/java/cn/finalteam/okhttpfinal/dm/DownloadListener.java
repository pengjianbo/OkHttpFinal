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

package cn.finalteam.okhttpfinal.dm;

/**
 * Desction:文件下载事件回调
 * Author:pengjianbo
 * Date:15/8/22 下午6:22
 */
public class DownloadListener {

    /**
     * 下载进行时回调
     * Callback when download in progress.
     *
     */
    public void onProgress(DownloadInfo downloadInfo) {

    }

    /**
     * 下载完成时回调
     * Callback when download finish.
     *
     */
    public void onFinish(DownloadInfo downloadInfo) {

    }

    /**
     * 下载出错时回调
     * Callback when download error.
     *
     */
    public void onError(DownloadInfo downloadInfo) {
    }
}
