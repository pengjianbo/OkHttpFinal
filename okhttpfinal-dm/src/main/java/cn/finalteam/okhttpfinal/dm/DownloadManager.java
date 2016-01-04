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

package cn.finalteam.okhttpfinal.dm;

import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.io.File;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午9:00
 */
public class DownloadManager {

    private static DownloadManager mDownloadManager;
    private ListenerManager mListenerManager;

    private DownloadManager(DownloadManagerConfig config){
        FileDownloader.init(config.getApplication());
        FileDownloadLog.NEED_LOG = config.isDebug();
        mListenerManager = new ListenerManager();
    }

    public static DownloadManager getInstance() {
        return mDownloadManager;
    }

    public static void init(DownloadManagerConfig config) {
        mDownloadManager = new DownloadManager(config);
    }

    private boolean verfyUrl(String url) {
        if ( StringUtils.isEmpty(url)) {
            Logger.d("download url null");
            return false;
        }
        return true;
    }

    public void addTask(String url, DownloadListener listener) {
        if (verfyUrl(url)) {
            BridgeListener bridgeListener = mListenerManager.getBridgeListener(url);
            bridgeListener.addDownloadListener(listener);
            File targetFile = new File("");
            FileDownloader.getImpl().create(url)
                    .setPath(targetFile.getAbsolutePath())
                    .setListener(bridgeListener)
                    .setAutoRetryTimes(3)
                    .start();
        }
    }

    /**
     * 添加一个任务事件回调
     * @param url
     * @param listener
     */
    public void addTaskListener(String url, DownloadListener listener) {
        if (verfyUrl(url)){
            BridgeListener bridgeListener = mListenerManager.getBridgeListener(url);
            bridgeListener.addDownloadListener(listener);
        }
    }
}
