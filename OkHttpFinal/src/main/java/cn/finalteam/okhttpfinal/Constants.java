/*
 * Copyright (C) 2015 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
