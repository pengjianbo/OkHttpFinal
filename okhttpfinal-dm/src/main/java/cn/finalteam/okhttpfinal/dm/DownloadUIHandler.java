package cn.finalteam.okhttpfinal.dm;

import android.os.Handler;
import android.os.Message;
import cn.finalteam.toolsfinal.Logger;
import java.util.List;
import java.util.Map;

/**
 * Desction:下载UI回调Handler
 * Author:pengjianbo
 * Date:15/8/27 下午9:13
 */
public class DownloadUIHandler extends Handler {

    private Map<String, List<DownloadListener>> mListenerListMap;
    private DownloadListener mGlobalDownloadListener;

    public DownloadUIHandler(Map<String, List<DownloadListener>> listenerListMap) {
        this.mListenerListMap = listenerListMap;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        DownloadInfo info = (DownloadInfo) msg.obj;
        if (info != null) {
            refreshDownloadInfo(info);
        } else {
            Logger.e("DownloadUIHandler DownloadInfo null");
        }
    }

    private void refreshDownloadInfo(DownloadInfo info) {
        List<DownloadListener> mListenerList = mListenerListMap.get(info.getUrl());
        if ( mGlobalDownloadListener != null ) {
            executeListener(mGlobalDownloadListener, info);
        }

        if ( mListenerList != null && mListenerList.size() > 0) {
            for (DownloadListener listener : mListenerList) {
                if (listener != null) {
                    executeListener(listener, info);
                }
            }
            return;
        }
    }

    private void executeListener(DownloadListener listener, DownloadInfo info) {
        int state = info.getState();
        switch (state) {
            case DownloadInfo.COMPLETE:
                listener.onFinish(info);
                break;
            case DownloadInfo.WAIT:
            case DownloadInfo.DOWNLOADING:
                listener.onProgress(info);
                break;
            case DownloadInfo.PAUSE:
                listener.onError(info);
                break;
            default:
                Logger.e("DownloadInfo state error");
        }
    }

    public void setGlobalDownloadListener(DownloadListener downloadListener) {
        this.mGlobalDownloadListener = downloadListener;
    }
}
