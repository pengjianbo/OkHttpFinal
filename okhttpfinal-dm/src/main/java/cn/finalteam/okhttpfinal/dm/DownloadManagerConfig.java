/*
 * Copyright (C) 2015 彭建波(pengjianbo@finalteam.cn), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.finalteam.okhttpfinal.dm;

import android.app.Application;
import cn.finalteam.toolsfinal.StorageUtils;
import cn.finalteam.toolsfinal.StringUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/23 下午9:04
 */
public class DownloadManagerConfig {

    private Application application;
    private String saveDir;
    private boolean debug;
    private int maxTask;

    private DownloadManagerConfig(Builder builder) {
        this.application = builder.application;
        this.debug = builder.debug;
        if (StringUtils.isEmpty(builder.saveDir)) {
            this.saveDir = StorageUtils.getCacheDirectory(application).getAbsolutePath() + "/download/";
        } else {
            this.saveDir = builder.saveDir;
        }
        this.maxTask = builder.maxTask;
    }

    public static class Builder{
        private Application application;
        private String saveDir;
        private boolean debug;
        private int maxTask = 3;

        public Builder(Application application) {
            this.application = application;
        }

        public Builder setSaveDir(String saveDir) {
            this.saveDir = saveDir;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setMaxTask(int maxTask) {
            this.maxTask = maxTask;
            return this;
        }
    }

    public Application getApplication() {
        return application;
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
