package cn.finalteam.okhttpfinal.sample.model;

import cn.finalteam.okhttpfinal.dm.BaseDownloadInfo;
import cn.finalteam.okhttpfinal.dm.DownloadInfo;
import cn.finalteam.sqlitefinal.annotation.Column;
import cn.finalteam.sqlitefinal.annotation.Foreign;
import cn.finalteam.sqlitefinal.annotation.Table;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/8 上午9:42
 */
@Table(name = "GameDownloadList")
public class MyDownloadInfo extends BaseDownloadInfo{

    @Column(column = "logo")
    private String logo;
    @Column(column = "appName")
    private String appName;
    @Column(column = "packageName")
    private String packageName;
    @Column(column = "gameId")
    private int gameId;

    @Foreign(column = "parentId", foreign = "id")
    private DownloadInfo downloadInfo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }
}
