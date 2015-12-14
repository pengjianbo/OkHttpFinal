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

package cn.finalteam.okhttpfinal.sample;

import android.os.Bundle;
import butterknife.Bind;
import butterknife.ButterKnife;
import us.feras.mdv.MarkdownView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/14 下午4:30
 */
public class OtherFuncationActivity extends BaseActivity {

    @Bind(R.id.mv_code)
    MarkdownView mMvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_function);
        ButterKnife.bind(this);

        mMvCode.loadMarkdownFile("file:///android_asset/OtherFuncation.md", "file:///android_asset/css-themes/classic.css");
    }
}
