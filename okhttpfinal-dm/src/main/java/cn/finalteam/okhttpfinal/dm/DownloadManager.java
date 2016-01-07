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

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;

import java.io.File;
import java.util.Date;
import java.util.List;

import cn.finalteam.okhttpfinal.dm.db.DBManager;
import cn.finalteam.okhttpfinal.dm.db.FileDownloadInfo;
import cn.finalteam.toolsfinal.DateUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StorageUtils;
import cn.finalteam.toolsfinal.StringUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午9:00
 */
public class DownloadManager {

    private static DownloadManager mDownloadManager;
    private ListenerManager mListenerManager;
    private File mSaveDir;
    private DBManager mDBManager;

    private DownloadManager(DownloadManagerConfig config){
        FileDownloader.init(config.getApplication());
        FileDownloadLog.NEED_LOG = config.isDebug();
        mDBManager = DBManager.getInstance(config.getApplication());
        mListenerManager = new ListenerManager(mDBManager);

        if ( StringUtils.isEmpty(config.getSaveDir()) ) {
            mSaveDir = new File(StorageUtils.getCacheDirectory(config.getApplication()), "download");
        } else {
            mSaveDir = new File(config.getSaveDir());
        }

        if (!mSaveDir.exists()) {
            mSaveDir.mkdirs();
        }
    }

    public static DownloadManager getInstance() {
        if(mDownloadManager == null) {
            Logger.e("Please init DownloadManager.");
        }
        return mDownloadManager;
    }

    public static void init(DownloadManagerConfig config) {
        mDownloadManager = new DownloadManager(config);
    }

    public List<FileDownloadInfo> getAllTask() {
        return mDBManager.getAllFileDownlod();
    }

    /**
     * 验证URL合法性
     * @param url
     * @return
     */
    private boolean verfyUrl(String url) {
        if ( StringUtils.isEmpty(url)) {
            Logger.d("download url null");
            return false;
        }
        return true;
    }

    /**
     * 添加任务
     * @param url
     * @param listener
     */
    public void addTask(String url, FileDownloadListener listener) {
        if (verfyUrl(url)) {
            String filename = FileUtils.getUrlFileName(url);
            if ( StringUtils.isEmpty(filename) ) {
                filename = DateUtils.format(new Date(), "yyyyMMddHHmmss");
            }

            addTask(url, new File(mSaveDir, filename), listener);
        }
    }

    /**
     * 添加任务
     * @param url
     * @param targetFile
     * @param listener
     */
    public void addTask(String url, File targetFile, FileDownloadListener listener) {
        if (verfyUrl(url)) {

            if (!exists(url)) {
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                }

                FileDownloadInfo info = new FileDownloadInfo();
                info.setUrl(url);
                info.setTargetPath(targetFile.getAbsolutePath());
                mDBManager.insert(info);

                BridgeListener bridgeListener = mListenerManager.getBridgeListener(url);
                bridgeListener.addDownloadListener(listener);
                FileDownloader.getImpl().create(url)
                        .setPath(targetFile.getAbsolutePath())
                        .setListener(bridgeListener)
                        .setAutoRetryTimes(3)
                        .start();
            } else {
                Logger.i("不能重复添加任务");
            }
        }
    }

    /**
     * 停止任务
     * @param url
     */
    public void stopTask(String url) {
        if (verfyUrl(url)) {
            BridgeListener bridgeListener = mListenerManager.getBridgeListener(url);
            FileDownloader.getImpl().pause(bridgeListener);
        }
    }

    /**
     * 重启任务
     * @param url
     */
    public void restarTask(String url, FileDownloadListener listener) {
        if (verfyUrl(url)) {
            BridgeListener bridgeListener = mListenerManager.getBridgeListener(url);
            if (listener != null) {
                bridgeListener.addDownloadListener(listener);
            }
            FileDownloader.getImpl().start(bridgeListener, false);
        }
    }

    public void restarTask(String url) {
        restarTask(url, null);
    }

    /**
     * 停止所有任务
     */
    public void stopAllTask() {
        FileDownloader.getImpl().pauseAll();
    }

    /**
     * 删除一个任务
     * @param url
     */
    public void deleteTask(String url) {
        stopTask(url);
        mListenerManager.removeAllDownloadListener(url);
        mDBManager.delete(url);
    }

    /**
     * 删除所有任务
     */
    public void deleteAllTask() {
        FileDownloader.getImpl().pauseAll();
        mListenerManager.removeAllDownloadListener();
    }

    /**
     * 添加一个任务事件回调
     * @param url
     * @param listener
     */
    public void addTaskListener(String url, FileDownloadListener listener) {
        if (verfyUrl(url)){
            BridgeListener bridgeListener = mListenerManager.getBridgeListener(url);
            bridgeListener.addDownloadListener(listener);
        }
    }

    /**
     * 判断是否存在
     * @param url
     * @return
     */
    public boolean exists(String url){
        return mDBManager.exists(url);
    }
}
