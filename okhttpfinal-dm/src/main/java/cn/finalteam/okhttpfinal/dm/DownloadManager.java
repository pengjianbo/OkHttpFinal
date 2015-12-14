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

import android.content.Context;
import android.text.TextUtils;
import cn.finalteam.sqlitefinal.DbHelper;
import cn.finalteam.sqlitefinal.exception.DbException;
import cn.finalteam.sqlitefinal.sqlite.WhereBuilder;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StorageUtils;
import cn.finalteam.toolsfinal.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Desction:下载管理
 * Author:pengjianbo
 * Date:15/8/23 下午5:42
 */
public class DownloadManager implements DownloadNextTaskListener {
    public static final int MAX_DOWNLOAD_COUNT = 3;//最大并行下载数

    private static DownloadManager sDownloadManager;

    private Map<String, DownloadHttpTask> mDownloadingTaskMap;

    private List<DownloadInfo> mAllTasks;
    //正在下载的列表，这个列表的大小取决于MAX_DOWNLOAD_COUNT值
    private List<DownloadInfo> mDownloadingTasks;
    //已暂停的列表(手动点击了暂停或网络原因引起下载失败退出应用程序)
    private List<DownloadInfo> mPausingTasks;
    //已完成的列表,用户点击安装后或编辑删除才能从这个集合移除掉
    private List<DownloadInfo> mCompleteTasks;
    //等待队列集合
    private ConcurrentLinkedQueue<DownloadInfo> mWaitTasks;

    //下载任务回调集合
    private Map<String, List<DownloadListener>> mListenerListMap;
    //下载目录
    private String mTargetFolder;
    private DbHelper mDbHelper;
    private DownloadUIHandler mDownloadUIHandler;
    private boolean mNextTaskLock;
    private Object mNextTaskLockObj = new Object();
    private Object mIteratorLock = new Object();

    //下载管理目标文件夹
    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator;

    public DownloadManager(Context context) {

        mAllTasks = Collections.synchronizedList(new ArrayList());
        mDownloadingTasks = Collections.synchronizedList(new ArrayList());
        mPausingTasks = Collections.synchronizedList(new ArrayList());
        mCompleteTasks = Collections.synchronizedList(new ArrayList());
        mWaitTasks = new ConcurrentLinkedQueue<>();
        mDownloadingTaskMap = Collections.synchronizedMap(new HashMap<String, DownloadHttpTask>());
        mListenerListMap = Collections.synchronizedMap(new HashMap<String, List<DownloadListener>>());
        mDownloadUIHandler = new DownloadUIHandler(mListenerListMap);

        //初始化目标Download保存目录
        String folder = StorageUtils.getCacheDirectory(context).getAbsolutePath() + DM_TARGET_FOLDER;
        if ( !new File(folder).exists() ) {
            FileUtils.makeFolders(folder);
        }
        mTargetFolder = folder;

        //构建下载Downlaod DbHelper
        mDbHelper = createDownloadDb(context);
        //获取所有任务
        if ( mDbHelper != null ) {
            try {
                List<DownloadInfo> list = mDbHelper.findAll(DownloadInfo.class);
                if ( list != null && list.size() > 0 ) {

                    for ( DownloadInfo info:list ) {
                        if ( info.getProgress() == 100 ) {
                            info.setState(DownloadInfo.COMPLETE);
                            mCompleteTasks.add(info);
                        } else {
                            info.setState(DownloadInfo.PAUSE);
                            mPausingTasks.add(info);
                        }
                    }

                    mAllTasks.addAll(mPausingTasks);
                    mAllTasks.addAll(mCompleteTasks);
                }
            } catch (DbException e) {
                Logger.e(e);
            }
        }
    }

    public static DownloadManager getInstance(Context context) {
        if (null == sDownloadManager) {
            sDownloadManager = new DownloadManager(context);
        }
        return sDownloadManager;
    }

    /**
     * 判断是否有这个任务
     * @param url
     * @return
     */
    public boolean hasTask(String url) {
        //判断下载中是否有
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setUrl(url);
        if ( mAllTasks.contains(downloadInfo) ) {
            return true;
        }
        return false;
    }

    /**
     * 是否有下载任务
     * @return
     */
    public boolean hasDownloadTask() {
        if ( mDownloadingTasks.size() > 0 || mPausingTasks.size() > 0
                || mWaitTasks.size() > 0 || mCompleteTasks.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 下载一个任务
     * @param url
     * @param listener
     */
    public void addTask(String url, DownloadListener listener) {
        if ( StringUtils.isEmpty(url)) {
            Logger.d("download url null");
            return;
        }

        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setUrl(url);
        if( !hasTask(url) ) {
            downloadInfo.setTargetFolder(mTargetFolder);
            try {
                mDbHelper.save(downloadInfo);
            } catch (DbException e) {
                Logger.e(e);
            }

            if ( mDownloadingTasks.size() < MAX_DOWNLOAD_COUNT ) {
                downloadInfo.setState(DownloadInfo.DOWNLOADING);
                mDownloadingTasks.add(downloadInfo);
                DownloadHttpTask task = new DownloadHttpTask(downloadInfo, mDownloadUIHandler, mDbHelper, this);
                mDownloadingTaskMap.put(url, task);
                task.start();
            } else { //加入等待队列
                downloadInfo.setState(DownloadInfo.WAIT);
                boolean b = mWaitTasks.offer(downloadInfo);
                if ( b ) {
                    addTaskListener(url, listener);
                }
            }
            mAllTasks.add(downloadInfo);
        } else {
            Logger.d("任务已存在");
        }
    }

    /**
     * 重新下载
     * @param url
     */
    public void restartTask(String url) {
        Iterator<DownloadInfo> pauseIt = mPausingTasks.iterator();
        if( mPausingTasks.size()>0) {
            synchronized(mIteratorLock) {
                while (pauseIt.hasNext()) {
                    DownloadInfo downloadInfo = pauseIt.next();
                    if (TextUtils.equals(downloadInfo.getUrl(), url)) {
                        if (mDownloadingTasks.size() < MAX_DOWNLOAD_COUNT) {
                            downloadInfo.setState(DownloadInfo.DOWNLOADING);
                            DownloadHttpTask task = new DownloadHttpTask(downloadInfo, mDownloadUIHandler, mDbHelper, this);
                            mDownloadingTaskMap.put(url, task);
                            mDownloadingTasks.add(downloadInfo);
                            pauseIt.remove();
                            task.start();
                        } else {
                            downloadInfo.setState(DownloadInfo.WAIT);
                            mWaitTasks.offer(downloadInfo);
                            pauseIt.remove();
                        }
                        return;
                    }
                }
            }
        }else {
            stopTask(url);
            restartTask(url);
        }
    }

    /**
     * 停止任务
     * @param url
     * @param remove
     */
    public void stopTask(String url, boolean remove) {
        //下载中停止task
        removeDownloadingMap(url);

        Iterator<DownloadInfo> downloadingIt =  mDownloadingTasks.iterator();
        while (downloadingIt.hasNext()) {
            DownloadInfo b = downloadingIt.next();
            if (TextUtils.equals(b.getUrl(), url)) {
                b.setState(DownloadInfo.PAUSE);
                downloadingIt.remove();
                if ( !remove ) {
                    mPausingTasks.add(b);//放入暂停队列
                }
                break;
            }
        }

        Iterator<DownloadInfo> waitIt = mWaitTasks.iterator();
        while (waitIt.hasNext()) {
            DownloadInfo b = waitIt.next();
            if (TextUtils.equals(b.getUrl(), url)) {
                b.setState(DownloadInfo.PAUSE);
                waitIt.remove();
                if ( !remove ) {
                    mPausingTasks.add(b);//放入暂停队列
                }
                break;
            }
        }
    }

    /**
     * 停止任务
     * @param url
     */
    public void stopTask(String url) {
        stopTask(url, false);
    }

    /**
     * 停止所有任务
     */
    public void stopAllTask() {
        //从下载队列移除
        for(Map.Entry<String, DownloadHttpTask> entry:mDownloadingTaskMap.entrySet()){
            DownloadHttpTask task = entry.getValue();
            task.setInterrupt(true);
        }
        mDownloadingTaskMap.clear();

        Iterator<DownloadInfo> downloadingIt = mDownloadingTasks.iterator();
        while (downloadingIt.hasNext()) {
            DownloadInfo b = downloadingIt.next();
            b.setState(DownloadInfo.PAUSE);
            downloadingIt.remove();
            mPausingTasks.add(b);//放入暂停队列
        }
        mDownloadingTasks.clear();

        //从等待队列移除
        Iterator<DownloadInfo> waitIt = mWaitTasks.iterator();
        while (waitIt.hasNext()) {
            DownloadInfo b = waitIt.next();
            b.setState(DownloadInfo.PAUSE);
            waitIt.remove();
            mPausingTasks.add(b);//放入暂停队列
            break;
        }
        mWaitTasks.clear();
    }

    /**
     * 删除任务
     * @param url
     */
    public void deleteTask(String url) {
        //从等待队列和下载中
        stopTask(url, true);
        //从已暂停队列移除
        Iterator<DownloadInfo> pausingIt = mPausingTasks.iterator();
        synchronized(mIteratorLock) {
            while (pausingIt.hasNext()) {
                DownloadInfo bean = pausingIt.next();
                if (TextUtils.equals(bean.getUrl(), url)) {
                    bean.setState(DownloadInfo.PAUSE);
                    pausingIt.remove();
                    break;
                }
            }
        }

        //从完成队列移除
        Iterator<DownloadInfo> completeIt = mCompleteTasks.iterator();
        synchronized(mIteratorLock) {
            while (completeIt.hasNext()) {
                DownloadInfo bean = completeIt.next();
                if (TextUtils.equals(bean.getUrl(), url)) {
                    bean.setState(DownloadInfo.PAUSE);
                    completeIt.remove();
                    break;
                }
            }
        }

        synchronized(mIteratorLock) {
            String filePath = "";
            //所有任务
            Iterator<DownloadInfo> allTaskIt = mAllTasks.iterator();
            while (allTaskIt.hasNext()) {
                DownloadInfo bean = allTaskIt.next();
                if (TextUtils.equals(bean.getUrl(), url)) {
                    bean.setState(DownloadInfo.PAUSE);
                    filePath = bean.getTargetPath();
                    allTaskIt.remove();
                    break;
                }
            }

            if ( !StringUtils.isEmpty(filePath) ) {
                FileUtils.deleteFile(filePath);
            }
        }


        synchronized(mIteratorLock) {
            //任务时间回调
            for (Map.Entry<String, List<DownloadListener>> entry : mListenerListMap.entrySet()) {
                List<DownloadListener> listenerList = entry.getValue();
                if (listenerList != null) {
                    listenerList.clear();
                    listenerList = null;
                }
            }
        }

        //清除数据库
        try {
            mDbHelper.delete(DownloadInfo.class, WhereBuilder.b("url", "=", url));
        } catch (DbException e) {
            Logger.e(e);
        }
    }

    private void executeNextTask() {
        if ( mDownloadingTasks.size() < MAX_DOWNLOAD_COUNT ) {
            if ( mWaitTasks.size() > 0 ) {
                DownloadInfo downloadInfo = mWaitTasks.poll();
                if (downloadInfo != null) {
                    String url = downloadInfo.getUrl();
                    downloadInfo.setState(DownloadInfo.DOWNLOADING);
                    DownloadHttpTask task = new DownloadHttpTask(downloadInfo, mDownloadUIHandler, mDbHelper, this);
                    mDownloadingTaskMap.put(url, task);
                    task.start();
                }
            }
        } else {
            Logger.d("已达到最大下载数量：" + mDownloadingTasks.size());
        }
    }

    @Override
    public void nextTask() {
        synchronized (mNextTaskLockObj) {
            if (!mNextTaskLock) {
                mNextTaskLock = true;
                //TODO mDownloadingTasks 同步状态(临时解决方案)
                synchronized(mIteratorLock) {
                    Iterator<DownloadInfo> downloadingIt = mDownloadingTasks.iterator();
                    while (downloadingIt.hasNext()) {
                        DownloadInfo b = downloadingIt.next();
                        if (b.getState() == DownloadInfo.PAUSE) {
                            mPausingTasks.add(b);
                            try {
                                downloadingIt.remove();
                            } catch (Exception e) {
                                Logger.e(e);
                            }
                        } else if (b.getState() == DownloadInfo.COMPLETE) {
                            mCompleteTasks.add(b);
                            try {
                                downloadingIt.remove();
                            } catch (Exception e) {
                                Logger.e(e);
                            }
                        }
                    }
                }
                synchronized(mIteratorLock) {
                    Iterator<Map.Entry<String, DownloadHttpTask>> downloadinMapIt = mDownloadingTaskMap.entrySet().iterator();
                    while (downloadinMapIt.hasNext()) {
                        Map.Entry<String, DownloadHttpTask> entry = downloadinMapIt.next();
                        DownloadHttpTask task = entry.getValue();
                        if (task != null && task.isInterrupt()) {
                            try {
                                downloadinMapIt.remove();
                            } catch (Exception e) {
                                Logger.e(e);
                            }
                        }
                    }
                }

                executeNextTask();
                mNextTaskLock = false;
            }
        }
    }

    /**
     * 根据URL获取DownloadInfo
     * @param url
     * @return
     */
    public DownloadInfo getDownloadByUrl(String url) {
        Iterator<DownloadInfo> allTaskIt = mAllTasks.iterator();
        while (allTaskIt.hasNext()) {
            DownloadInfo bean = allTaskIt.next();
            if (TextUtils.equals(bean.getUrl(), url)) {
                return bean;
            }
        }

        return null;
    }

    private synchronized void removeDownloadingMap(String url) {
        synchronized(mIteratorLock) {
            Iterator<Map.Entry<String, DownloadHttpTask>> it = mDownloadingTaskMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, DownloadHttpTask> entry = it.next();
                String key = entry.getKey();
                DownloadHttpTask task = entry.getValue();
                if (TextUtils.equals(key, url)) {
                    if (task != null) {
                        task.setInterrupt(true);
                    }
                    try {
                        it.remove();
                    } catch (Exception e) {
                        Logger.e(e);
                    }
                    break;
                }
            }
        }
    }

    public List<DownloadInfo> getAllTask() {
        Collections.sort(mAllTasks);
        return mAllTasks;
    }

    public int getDownloadingSize() {
        return mDownloadingTasks.size();
    }

    /**
     * 为一个任务添加事件回调
     * @param url
     * @param listener
     */
    public List<DownloadListener> addTaskListener(String url, DownloadListener listener) {
        List<DownloadListener> listenerList = mListenerListMap.get(url);
        if ( listenerList == null  ) {
            listenerList = new ArrayList<>();
        }
        if ( listener != null ) {
            listenerList.add(listener);
        }
        mListenerListMap.put(url, listenerList);

        return listenerList;
    }

    /**
     * 清除下载回调
     * @param url
     */
    public void clearTaskListener(String url) {
        List<DownloadListener> listenerList = mListenerListMap.get(url);
        if ( listenerList != null  ) {
            listenerList.clear();
        }
    }

    /**
     * 清除所有下载回调
     */
    public void clearAllTaskListener() {
        mListenerListMap.clear();
    }

    /**
     * 设置全局下载监听器
     * @param globalDownloadListener
     */
    public void setGlobalDownloadListener(DownloadListener globalDownloadListener) {
        mDownloadUIHandler.setGlobalDownloadListener(globalDownloadListener);
    }

    private DbHelper createDownloadDb(Context context) {
        try {
            DbHelper.DaoConfig config = new DbHelper.DaoConfig(context);
            config.setDbName("download.db");
            config.setDbVersion(1);
            DbHelper dbHelper = DbHelper.create(config);
            return dbHelper;
        }catch (Exception e) {
            Logger.e(e);
        }

        return null;
    }

}
