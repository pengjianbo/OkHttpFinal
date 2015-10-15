package cn.finalteam.okhttpfinal.dm;

import android.text.TextUtils;
import cn.finalteam.sqlitefinal.annotation.Column;
import cn.finalteam.sqlitefinal.annotation.Id;
import cn.finalteam.sqlitefinal.annotation.Table;
import cn.finalteam.sqlitefinal.annotation.Transient;

/**
 * Desction:文件下载数据模型
 * Author:pengjianbo
 * Date:15/8/22 下午5:11
 */
@Table(name = "DownloadInfo")
public class DownloadInfo implements Comparable<DownloadInfo>{

    //==============State=================
    public static final int WAIT = 0;//等待
    public static final int DOWNLOADING = 1;//下载中
    public static final int PAUSE = 2;//暂停
    public static final int COMPLETE = 3;//完成

    @Id
    @Column(column = "id")
    private int id;
    @Column(column = "url")
    private String url;//文件URL
    @Column(column = "targetPath")
    private String targetPath;//保存文件地址
    @Column(column = "targetFolder")
    private String targetFolder;//保存文件夹
    @Column(column = "progress")
    private int progress;//下载进度
    @Column(column = "totalLength")
    private long totalLength;//总大小
    @Column(column = "downloadLength")
    private long downloadLength;//已下载大小

    @Transient
    private long networkSpeed;//下载速度
    @Transient
    private int state = PAUSE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public long getDownloadLength() {
        return downloadLength;
    }

    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    public long getNetworkSpeed() {
        return networkSpeed;
    }

    public void setNetworkSpeed(long networkSpeed) {
        this.networkSpeed = networkSpeed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if ( o instanceof DownloadInfo ) {
            DownloadInfo info = (DownloadInfo) o;
            if ( info != null && TextUtils.equals(info.getUrl(), url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(DownloadInfo another) {
        if ( another == null ) {
            return 0;
        }

        int lhs = getId();
        int rhs = another.getId();
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }
}
