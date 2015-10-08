package cn.finalteam.okhttpfinal.sample.http.model;

import android.text.TextUtils;
import cn.finalteam.okhttpfinal.ApiResponse;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/26 下午10:56
 */
public class GameInfo implements ApiResponse {

    private int gameId;
    private String name;
    private String packageName;
    private String gameState;
    private boolean hasActive;
    private boolean hasGift;
    private int commentCount;
    private String iconUrl;
    private int isHot;
    private int jingCount;
    private int playerCount;
    private int resourceId;
    private int totalSocre;
    private int openState;//开放状态
    private String coverUrl;//封面图片

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public boolean isHasActive() {
        return hasActive;
    }

    public void setHasActive(boolean hasActive) {
        this.hasActive = hasActive;
    }

    public boolean isHasGift() {
        return hasGift;
    }

    public void setHasGift(boolean hasGift) {
        this.hasGift = hasGift;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getIconUrl() {

        if(!TextUtils.isEmpty(this.coverUrl)){
            return coverUrl;
        }

        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getJingCount() {
        return jingCount;
    }

    public void setJingCount(int jingCount) {
        this.jingCount = jingCount;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getTotalSocre() {
        return totalSocre;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
    public float getTotalSocreV() {
        if(this.totalSocre > 0){

            return this.totalSocre / 10.0f;
        }
        return 0f;
    }


    public void setTotalSocre(int totalSocre) {
        this.totalSocre = totalSocre;
    }

    public int getOpenState() {
        return openState;
    }

    public void setOpenState(int openState) {
        this.openState = openState;
    }

    /**
     * 获取游戏的星级，转换
     * @return
     */
    public float getRating(){
        if(this.totalSocre!=0){
            float rating = this.totalSocre / 20.0f;
            return rating;
        }
        return 0f;
    }
}

