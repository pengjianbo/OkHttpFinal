package cn.finalteam.okhttpfinal.dm.v2;

import cn.aigestudio.downloader.interfaces.IDListener;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/24 上午11:09
 */
class ListenerManager {
    private ConcurrentHashMap<String, IDListener> mListenerListMap;
    private ListenerManager() {
        mListenerListMap = new ConcurrentHashMap<>();
    }

    public IDListener getIDListener(String url) {
        IDListener listener = mListenerListMap.get(url);
        if ( listener == null ) {
            listener = new BridgeListener();
        }

        return listener;
    }
}
