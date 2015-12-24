package cn.finalteam.okhttpfinal.dm.v2;

import cn.aigestudio.downloader.bizs.DLManager;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午9:00
 */
public class DownloadManager {

    private DLManager mDLManager;
    private String mSaveDir;

    private ConcurrentHashMap<String, List<DownloadListener>> mListenerListMap;

    private DownloadManager(){
    }

    public void init(DownloadManagerConfig config) {
        mDLManager = DLManager.getInstance(config.getContext());
        mDLManager.setDebugEnable(config.isDebug());
        mDLManager.setMaxTask(config.getMaxTask());
        mListenerListMap = new ConcurrentHashMap<>();
    }

    public void addTaskListener(String url, DownloadListener listener) {

    }

    public void addTask(String url, DownloadListener listener) {
        mDLManager.dlStart(url, mSaveDir, listener.getDlListener());
    }

}
