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

package cn.finalteam.okhttpfinal.dm.v2;

import cn.aigestudio.downloader.interfaces.IDListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/24 上午12:46
 */
class BridgeListener implements IDListener{

    private List<DownloadListener> mListenerList;

    public BridgeListener(){
        mListenerList = Collections.synchronizedList(new ArrayList<DownloadListener>());
    }

    public void addDownloadListener(DownloadListener listener) {
        if ( listener != null && !mListenerList.contains(listener)) {
            mListenerList.add(listener);
        }
    }

    @Override
    public void onPrepare() {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onPrepare();
            }
        }
    }

    @Override
    public void onStart(String fileName, String realUrl, int fileLength) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onStart(fileName, realUrl, fileLength);
            }
        }
    }

    @Override
    public void onProgress(int progress) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onProgress(progress);
            }
        }
    }

    @Override
    public void onStop(int progress) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onStop(progress);
            }
        }
    }

    @Override
    public void onFinish(File file) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onFinish(file);
            }
        }
    }

    @Override
    public void onError(int status, String error) {
        for(DownloadListener listener: mListenerList) {
            if (listener != null) {
                listener.onError(status, error);
            }
        }
    }
}
