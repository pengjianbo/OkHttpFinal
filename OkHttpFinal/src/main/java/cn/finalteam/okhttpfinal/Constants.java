package cn.finalteam.okhttpfinal;

import java.io.File;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/21 上午9:54
 */
public class Constants {
    protected static boolean DEBUG = BuildConfig.DEBUG;
    //下载管理目标文件夹
    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator;
    //Http请求超时时间
    public static final int REQ_TIMEOUT = 30000;
}
