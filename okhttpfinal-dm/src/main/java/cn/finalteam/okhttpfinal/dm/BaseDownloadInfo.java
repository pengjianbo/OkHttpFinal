package cn.finalteam.okhttpfinal.dm;

import cn.finalteam.sqlitefinal.annotation.Column;
import cn.finalteam.sqlitefinal.annotation.Id;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/8 上午9:48
 */
public class BaseDownloadInfo {

    @Id
    @Column(column = "id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
