package cn.finalteam.okhttpfinal.dm.v2;

import android.content.Context;
import cn.finalteam.toolsfinal.StorageUtils;
import cn.finalteam.toolsfinal.StringUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午9:04
 */
public class DownloadManagerConfig {

    private Context context;
    private String saveDir;
    private boolean debug;
    private int maxTask;

    private DownloadManagerConfig(Builder builder) {
        this.context = builder.context;
        if (StringUtils.isEmpty(builder.saveDir)) {
            this.saveDir = StorageUtils.getCacheDirectory(context).getAbsolutePath() + "/download/";
        } else {
            this.saveDir = builder.saveDir;
        }
        this.maxTask = builder.maxTask;
    }

    public static class Builder{

        private Context context;
        private String saveDir;
        private boolean debug;
        private int maxTask = 3;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setSaveDir(String saveDir) {
            this.saveDir = saveDir;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public void setMaxTask(int maxTask) {
            this.maxTask = maxTask;
        }
    }

    public Context getContext() {
        return context;
    }

    public String getSaveDir() {
        return saveDir;
    }

    public boolean isDebug() {
        return debug;
    }

    public int getMaxTask() {
        return maxTask;
    }
}
