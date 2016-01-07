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

package cn.finalteam.okhttpfinal.dm.db;

import android.content.Context;

import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/1/7 0007 17:17
 */
public class DBManager {
    private static DBManager mInstance;
    private final FileDownloadInfoDao mFileDownloadInfoDao;

    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBManager(context);
        }
        return mInstance;
    }

    private DBManager(Context context) {
        mFileDownloadInfoDao = new FileDownloadInfoDao(context);
    }

    public synchronized void insert(FileDownloadInfo threadInfo) {
        mFileDownloadInfoDao.insert(threadInfo);
    }

    public synchronized void delete(String url) {
        mFileDownloadInfoDao.delete(url);
    }

    public synchronized void update(String url, long downloadLength, long totalLength, int progress) {
        mFileDownloadInfoDao.update(url, downloadLength, totalLength, progress);
    }

    public FileDownloadInfo getFileDownload(String url) {
        return mFileDownloadInfoDao.getFileDownload(url);
    }

    public List<FileDownloadInfo> getAllFileDownlod() {
        return mFileDownloadInfoDao.getAllFileDownlod();
    }

    public List<FileDownloadInfo> getAllFileDownlodFinish() {
        return mFileDownloadInfoDao.getAllFileDownlodFinish();
    }

    public boolean exists(String url) {
        return mFileDownloadInfoDao.exists(url);
    }
}
