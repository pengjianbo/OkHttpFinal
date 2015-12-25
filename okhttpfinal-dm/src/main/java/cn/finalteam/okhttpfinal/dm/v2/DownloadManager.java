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

import cn.aigestudio.downloader.bizs.DLManager;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午9:00
 */
public class DownloadManager {

    private DLManager mDLManager;
    private String mSaveDir;
    private ListenerManager mListenerManager;

    private DownloadManager(){
    }

    public void init(DownloadManagerConfig config) {
        mDLManager = DLManager.getInstance(config.getContext());
        mDLManager.setDebugEnable(config.isDebug());
        mDLManager.setMaxTask(config.getMaxTask());
        mListenerManager = new ListenerManager();
    }

    /**
     * 添加一个任务事件回调
     * @param url
     * @param listener
     */
    public void addTaskListener(String url, DownloadListener listener) {
    }

    /**
     * 添加新任务
     * @param url
     * @param listener
     */
    public void addTask(String url, DownloadListener listener) {
        if ( StringUtils.isEmpty(url)) {
            Logger.d("download url null");
            return;
        }

        BridgeListener bridgeListener = mListenerManager.getBridgeListener(url);
        bridgeListener.addDownloadListener(listener);

        mDLManager.dlStart(url, mSaveDir, bridgeListener);//开始执行任务
    }


}
