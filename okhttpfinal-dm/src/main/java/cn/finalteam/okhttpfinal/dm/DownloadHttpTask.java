package cn.finalteam.okhttpfinal.dm;

import android.os.Message;
import cn.finalteam.okhttpfinal.OkHttpFactory;
import cn.finalteam.sqlitefinal.DbHelper;
import cn.finalteam.sqlitefinal.exception.DbException;
import cn.finalteam.sqlitefinal.sqlite.WhereBuilder;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Desction: TODO 代码稍微有乱，下次把状态同步的问题解决
 * Author:pengjianbo
 * Date:15/8/22 下午4:30
 */
public class DownloadHttpTask extends Thread {

    private final int BUFFER_SIZE = 1024 * 8;

    private int RESULT_NET_ERROR = -1;
    private int RESULT_OTHER_ERROR = 0;
    private int RESULT_SUCCESS = 1;

    //任务终止标记
    private boolean mInterrupt;
    private ProgressReportingRandomAccessFile mProgressReportingRandomAccessFile;
    //开始下载时间，用户计算加载速度
    private long mPreviousTime;
    //更新数据标记或叫开关
    private boolean mUpdateDbFlag;

    private DownloadInfo mDownloadInfo;
    private UpdateDownloadDbThread mUpdateDbThread;
    private DownloadNextTaskListener mDownloadNextTaskListener;
    private DownloadUIHandler mDownloadUIHandler;

    private OkHttpClient mOkHttpClient;
    private DbHelper mDbHelper;

    public DownloadHttpTask(DownloadInfo downloadInfo, DownloadUIHandler downloadUIHandler, DbHelper dbHelper, DownloadNextTaskListener downloadNextTaskListener) {
        this.mDownloadInfo = downloadInfo;
        this.mDownloadUIHandler = downloadUIHandler;
        this.mDbHelper = dbHelper;
        this.mOkHttpClient = OkHttpFactory.getOkHttpClientFactory(30000);
        this.mUpdateDbThread = new UpdateDownloadDbThread();
        this.mDownloadNextTaskListener = downloadNextTaskListener;
    }

    @Override
    public void run() {
        super.run();
        onPreExecute();

        int result = request();

        if ( result == RESULT_SUCCESS ) {
            mDownloadInfo.setState(DownloadInfo.COMPLETE);
        } else {
            mDownloadInfo.setState(DownloadInfo.PAUSE);
            mInterrupt = true;
        }

        mDownloadNextTaskListener.nextTask();

        postMessage();
    }

    private int request() {
        String url = mDownloadInfo.getUrl();
        int resultCode = RESULT_SUCCESS;
        long startPos = 0;
        String fileName = FileUtils.getUrlFileName(url);
        File file = new File(mDownloadInfo.getTargetFolder(), fileName);
        if (StringUtils.isEmpty(mDownloadInfo.getTargetPath())) {
            mDownloadInfo.setTargetPath(file.getAbsolutePath());
        }
        if ( file.exists() ) {
            startPos = file.length();
        } else {
            try {
                boolean b = file.createNewFile();
                if ( !b ) {
                    resultCode = RESULT_OTHER_ERROR;
                    Logger.e("create new File failure file=" + file.getAbsolutePath());
                    return resultCode;
                }
            } catch (IOException e) {
                Logger.e(e + " file=" + file.getAbsolutePath());
                resultCode = RESULT_OTHER_ERROR;
                return resultCode;
            }
        }

        //设置断点写文件
        try {
            mProgressReportingRandomAccessFile = new ProgressReportingRandomAccessFile(file, "rw", startPos);
        } catch (FileNotFoundException e) {
            Logger.e(e);
            resultCode = RESULT_OTHER_ERROR;
            return resultCode;
        }
        Request request = new Request.Builder()
                .url(url)
                .header("RANGE", "bytes=" + startPos + "-")//设置http断点RANGE值
                .build();

        //执行请求
        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
        } catch (IOException e) {
            Logger.e(e);
            resultCode = RESULT_OTHER_ERROR;
            return resultCode;
        }

        if (response == null || !response.isSuccessful()){//提示网络异常
            if ( response != null ) {
                Logger.e("下载文件失败了~ code=" + response.code() + "url=" + url);
            }
            resultCode = RESULT_NET_ERROR;
        } else {

            try {
                InputStream inputStream = response.body().byteStream();
                try {
                    long totalLength = response.body().contentLength();
                    if ( mDownloadInfo.getTotalLength() == 0l ) {
                        mDownloadInfo.setTotalLength(totalLength);
                    }

                    //文件大小大于总文件大小
                    if ( startPos > mDownloadInfo.getTotalLength() ) {
                        FileUtils.deleteFile(mDownloadInfo.getTargetPath());
                        mDownloadInfo.setProgress(0);
                        mDownloadInfo.setDownloadLength(0);
                        mDownloadInfo.setTotalLength(0);
                        return resultCode;
                    }

                    if ( startPos == mDownloadInfo.getTotalLength() && startPos > 0 ) {
                        publishProgress(100);
                        return resultCode;
                    }

                    //读写文件流
                    int bytesCopied = download(inputStream, mProgressReportingRandomAccessFile);
                    if (((startPos + bytesCopied) != mDownloadInfo.getTotalLength()) || mInterrupt) {
                        //下载失败
                        resultCode = RESULT_OTHER_ERROR;
                        return resultCode;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultCode = RESULT_OTHER_ERROR;
                    return resultCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                resultCode = RESULT_OTHER_ERROR;
                return resultCode;
            }
        }
        return resultCode;
    }

    /**
     * 执行文件下载
     * @param input
     * @param out
     * @return
     * @throws Exception
     */
    public int download(InputStream input, RandomAccessFile out) throws Exception {
        if (input == null || out == null) {
            return -1;
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        int count = 0, n = 0;

        try {
            out.seek(out.length());
            while (!mInterrupt) {
                n = in.read(buffer, 0, BUFFER_SIZE);
                if (n == -1) {
                    break;
                }
                if ( mInterrupt ) {
                    throw new RuntimeException("task interrupt");
                }
                out.write(buffer, 0, n);
                count += n;
            }
        } finally {
            mOkHttpClient = null;
            try {
                out.close();
                in.close();
                input.close();
            } catch (Exception e) {
                Logger.e(e);
            }
        }
        return count;
    }

    /**
     * 预下载
     */
    protected void onPreExecute() {
        mPreviousTime = System.currentTimeMillis();
        mDownloadInfo.setState(DownloadInfo.DOWNLOADING);
        mUpdateDbThread.start();

        postMessage();
    }

    /**
     * 更新UI
     * @param progress
     */
    public void publishProgress(int progress) {
        mDownloadInfo.setProgress(progress);
        if ( !mInterrupt ) {
            if (progress == 100) { //下载完成
                mDownloadInfo.setState(DownloadInfo.COMPLETE);
                mDownloadNextTaskListener.nextTask();
            } else {
                mDownloadInfo.setState(DownloadInfo.DOWNLOADING);
            }
        }

        mUpdateDbFlag = true;

        postMessage();
    }

    private void postMessage() {
        Message msg = mDownloadUIHandler.obtainMessage();
        msg.obj = mDownloadInfo;
        mDownloadUIHandler.sendMessage(msg);
    }

    public boolean isInterrupt() {
        return mInterrupt;
    }

    public void setInterrupt(boolean interrupt) {
        this.mInterrupt = interrupt;
        if ( mInterrupt ) {
            mDownloadInfo.setState(DownloadInfo.PAUSE);

            postMessage();

            mDownloadNextTaskListener.nextTask();
        }
    }

    /**
     * 文件读写
     */
    private final class ProgressReportingRandomAccessFile extends RandomAccessFile {
        private long lastDownloadLength = 0;
        private long curDownloadLength = 0;
        private long lastRefreshUiTime;

        public ProgressReportingRandomAccessFile(File file, String mode, long lastDownloadLength)
                throws FileNotFoundException {
            super(file, mode);
            this.lastDownloadLength = lastDownloadLength;
            this.lastRefreshUiTime = System.currentTimeMillis();
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            super.write(buffer, offset, count);

            //已下载大小
            long downloadLength = lastDownloadLength + count;
            curDownloadLength += count;
            lastDownloadLength = downloadLength;
            mDownloadInfo.setDownloadLength(downloadLength);

            //计算下载速度
            long totalTime = (System.currentTimeMillis() - mPreviousTime)/1000;
            if ( totalTime == 0 ) {
                totalTime += 1;
            }
            long networkSpeed = curDownloadLength / totalTime;
            mDownloadInfo.setNetworkSpeed(networkSpeed);

            //下载进度
            int progress = (int)(downloadLength * 100 / mDownloadInfo.getTotalLength());
            mDownloadInfo.setProgress(progress);
            long curTime = System.currentTimeMillis();
            if ( curTime - lastRefreshUiTime >= 1000 || progress == 100) {
                publishProgress(progress);
                lastRefreshUiTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * 更新数据库线程
     */
    private final class UpdateDownloadDbThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (true) {
                if ( mUpdateDbFlag ) {
                    mUpdateDbFlag = false;
                    //保存下载进度
                    try {
                        mDbHelper.update(mDownloadInfo, WhereBuilder.b("url", "=", mDownloadInfo.getUrl()), "progress", "downloadLength", "totalLength", "targetPath");
                    } catch (DbException e) {
                        Logger.e(e);
                    }
                }

                if ( mInterrupt ) {//终止
                    break;
                }
            }
        }
    }
}
