package cn.finalteam.okhttpfinal.dm;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/1/3 下午11:14
 */
class BridgeListener extends FileDownloadListener {

    private List<DownloadListener> mListenerList;

    public BridgeListener(){
        mListenerList = Collections.synchronizedList(new ArrayList<DownloadListener>());
    }

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                int preProgress = (soFarBytes * 100 / totalBytes);
                listener.onStart(task, preProgress);
            }
        }
    }

    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                int preProgress = (soFarBytes * 100 / totalBytes);
                listener.onProgress(task, preProgress);
            }
        }
    }

    @Override
    protected void blockComplete(BaseDownloadTask task) {
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onFinish(task);
            }
        }
    }

    @Override
    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onStop(task);
            }
        }
    }

    @Override
    protected void error(BaseDownloadTask task, Throwable e) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onStop(task);
            }
        }
    }

    @Override
    protected void warn(BaseDownloadTask task) {
    }

    public void addDownloadListener(DownloadListener listener) {
        if ( listener != null && !mListenerList.contains(listener)) {
            mListenerList.add(listener);
        }
    }

    public void removeDownloadListener(DownloadListener listener) {
        if ( listener != null && !mListenerList.contains(listener)) {
            mListenerList.remove(listener);
        }
    }

    public void removeAllDownloadListener() {
        mListenerList.clear();
    }

}