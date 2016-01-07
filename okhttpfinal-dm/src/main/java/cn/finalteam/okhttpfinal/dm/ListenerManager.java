package cn.finalteam.okhttpfinal.dm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.finalteam.okhttpfinal.dm.db.DBManager;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/1/3 下午11:59
 */
class ListenerManager {
    private ConcurrentHashMap<String, BridgeListener> mListenerListMap;
    private DBManager mDBManager;

    protected ListenerManager(DBManager dbManager) {
        mListenerListMap = new ConcurrentHashMap<>();
        mDBManager = dbManager;
    }

    public BridgeListener getBridgeListener(String url) {
        BridgeListener listener = mListenerListMap.get(url);
        if ( listener == null ) {
            listener = new BridgeListener(mDBManager);
        }
        mListenerListMap.put(url, listener);
        return listener;
    }

    public void removeDownloadListener(String url, FileDownloadListener dlistener) {
        BridgeListener listener = mListenerListMap.get(url);
        if ( listener != null ) {
            listener.removeDownloadListener(dlistener);
        }
    }

    public void removeAllDownloadListener(String url) {
        BridgeListener listener = mListenerListMap.get(url);
        if ( listener != null ) {
            listener.removeAllDownloadListener();
        }
    }

    public void removeAllDownloadListener() {
        for (Map.Entry<String, BridgeListener> entry : mListenerListMap.entrySet()) {
            BridgeListener listener = entry.getValue();
            if (listener != null) {
                listener.removeAllDownloadListener();
            }
        }
        mListenerListMap.clear();
    }
}
