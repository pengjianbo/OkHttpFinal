package cn.finalteam.okhttpfinal.dm.v2;

import cn.aigestudio.downloader.interfaces.IDListener;
import java.io.File;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午11:16
 */
public class DownloadListener {

    private IDListener listener;

    public DownloadListener() {
        listener = new IDListener() {
            @Override
            public void onPrepare() {
                DownloadListener.this.onPrepare();
            }

            @Override
            public void onStart(String fileName, String realUrl, int fileLength) {
                DownloadListener.this.onStart(fileName, realUrl, fileLength);
            }

            @Override
            public void onProgress(int progress) {
                DownloadListener.this.onProgress(progress);
            }

            @Override
            public void onStop(int progress) {
                DownloadListener.this.onStop(progress);
            }

            @Override
            public void onFinish(File file) {
                DownloadListener.this.onFinish(file);
            }

            @Override
            public void onError(int status, String error) {
                DownloadListener.this.onError(status, error);
            }
        };
    }

    public IDListener getDlListener() {
        return listener;
    }

    public void onPrepare() {

    }

    public void onStart(String fileName, String realUrl, int fileLength) {

    }

    public void onProgress(int progress) {

    }

    public void onStop(int progress) {

    }

    public void onFinish(File file) {

    }

    public void onError(int status, String error) {

    }

}
