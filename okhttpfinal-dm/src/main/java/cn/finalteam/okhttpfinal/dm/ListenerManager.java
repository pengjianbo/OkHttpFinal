package cn.finalteam.okhttpfinal.dm;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/1/3 下午11:59
 */
public class ListenerManager {
    private ConcurrentHashMap<String, BridgeListener> mListenerListMap;

    protected ListenerManager() {
        mListenerListMap = new ConcurrentHashMap<>();
    }

    public BridgeListener getBridgeListener(String url) {
        BridgeListener listener = mListenerListMap.get(url);
        if ( listener == null ) {
            listener = new BridgeListener();
        }
        mListenerListMap.put(url, listener);
        return listener;
    }


    public void removeDownloadListener(String url, DownloadListener dlistener) {
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
}
