package cn.finalteam.okhttpfinal.sample.model;

import java.io.Serializable;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/8 上午9:42
 */
public class GameDownloadInfo implements Serializable{

    private String logo;
    private String appName;
    private String packageName;
    private int gameId;

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

}
