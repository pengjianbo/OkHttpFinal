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

import java.util.concurrent.ConcurrentHashMap;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/24 上午11:09
 */
class ListenerManager {
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
