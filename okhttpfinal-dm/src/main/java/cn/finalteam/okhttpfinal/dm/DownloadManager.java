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

import cn.aigestudio.downloader.bizs.DLInfo;
import cn.aigestudio.downloader.bizs.DLManager;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午9:00
 */
public class DownloadManager {

    private DLManager mDLManager;
    private String mSaveDir;
    private ListenerManager mListenerManager;

    private static DownloadManager mDownloadManager;

    private DownloadManager(DownloadManagerConfig config){
        mDLManager = DLManager.getInstance(config.getContext());
        mDLManager.setDebugEnable(config.isDebug());
        mDLManager.setMaxTask(config.getMaxTask());
        mListenerManager = new ListenerManager();
    }

    public static DownloadManager getInstance() {
        return mDownloadManager;
    }

    public static void init(DownloadManagerConfig config) {
        mDownloadManager = new DownloadManager(config);
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

    public void removeTaskListener(String url, DownloadListener listener) {
        if(verfyUrl(url)) {
            mListenerManager.removeDownloadListener(url, listener);
        }
    }

    public void removeTaskAllListener(String url) {
        if(verfyUrl(url)) {
            mListenerManager.removeAllDownloadListener(url);
        }
    }

    /**
     * 开始一个任务
     * @param url
     * @param listener
     */
    public void startTask(String url, DownloadListener listener) {
        if(verfyUrl(url)) {
            BridgeListener bridgeListener = mListenerManager.getBridgeListener(url);
            bridgeListener.addDownloadListener(listener);
            mDLManager.dlStart(url, mSaveDir, bridgeListener);//开始执行任务
        }
    }

    /**
     * 暂停任务
     * @param url
     */
    public void stopTask(String url) {
        if(verfyUrl(url)) {
            mDLManager.dlStop(url);
        }
    }

    /**
     * 取消任务
     * @param url
     */
    public void cancellTask(String url) {
        if(verfyUrl(url)) {
            mDLManager.dlCancel(url);
        }
    }

    /**
     * 判断任务是否存在
     * @param url
     * @return
     */
    public boolean hasTask(String url) {
        DLInfo info = getTaskInfo(url);
        if(info == null) {
            return false;
        }

        return true;
    }

    /**
     * 任务信息
     * @param url
     * @return
     */
    public DLInfo getTaskInfo(String url) {
        DLInfo dlInfo = mDLManager.getDLInfo(url);
        return dlInfo;
    }

    private boolean verfyUrl(String url) {
        if ( StringUtils.isEmpty(url)) {
            Logger.d("download url null");
            return false;
        }
        return true;
    }
}
