package cn.finalteam.okhttpfinal.sample.http.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/26 下午11:10
 */
public class NewGameResponse extends BaseApiResponse{

    @SerializedName("data")
    private List<GameInfo> gameList;

    public List<GameInfo> getGameList() {
        return gameList;
    }

    public void setGameList(List<GameInfo> gameList) {
        this.gameList = gameList;
    }
}
