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

package cn.aigestudio.downloader.bizs;

import android.content.Context;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2015/12/31 0031 15:32
 */
public class DBManager {

    private DLDBManager mDLDBManager;

    public DBManager(Context context) {
        mDLDBManager = DLDBManager.getInstance(context);
    }


}
