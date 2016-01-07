package cn.finalteam.okhttpfinal.dm;

import com.liulishuo.filedownloader.BaseDownloadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.finalteam.okhttpfinal.dm.db.DBManager;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/1/3 下午11:14
 */
class BridgeListener extends com.liulishuo.filedownloader.FileDownloadListener {

    private List<FileDownloadListener> mListenerList;
    private DBManager mDBManager;

    public BridgeListener(DBManager dbManager){
        mListenerList = Collections.synchronizedList(new ArrayList<FileDownloadListener>());
        mDBManager = dbManager;
    }

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        for(FileDownloadListener listener: mListenerList) {
            if (listener != null) {
                int preProgress = 0;
                if ( totalBytes != 0 ) {
                    preProgress = (soFarBytes * 100 / totalBytes);
                }
                listener.onStart(task, preProgress);
            }
        }
    }

    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        int progress = 0;
        if ( totalBytes != 0 ) {
            progress = (soFarBytes * 100 / totalBytes);
        }
        for(FileDownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onProgress(task, progress);
            }
        }

        mDBManager.update(task.getUrl(), soFarBytes, totalBytes, progress);
    }

    @Override
    protected void blockComplete(BaseDownloadTask task) {
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        for(FileDownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onFinish(task);
            }
        }
    }

    @Override
    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        for(FileDownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onStop(task);
            }
        }
    }

    @Override
    protected void error(BaseDownloadTask task, Throwable e) {
        for(FileDownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onStop(task);
            }
        }
    }

    @Override
    protected void warn(BaseDownloadTask task) {
    }

    public void addDownloadListener(FileDownloadListener listener) {
        if ( listener != null && !mListenerList.contains(listener)) {
            mListenerList.add(listener);
        }
    }

    public void removeDownloadListener(FileDownloadListener listener) {
        if ( listener != null && !mListenerList.contains(listener)) {
            mListenerList.remove(listener);
        }
    }

    public void removeAllDownloadListener() {
        mListenerList.clear();
    }

}