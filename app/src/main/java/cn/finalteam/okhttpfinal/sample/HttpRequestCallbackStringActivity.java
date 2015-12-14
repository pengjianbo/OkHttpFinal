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
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.okhttpfinal.StringHttpRequestCallback;
import cn.finalteam.okhttpfinal.sample.http.Api;
import cn.finalteam.toolsfinal.JsonFormatUtils;
import us.feras.mdv.MarkdownView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/14 下午4:17
 */
public class HttpRequestCallbackStringActivity extends BaseActivity {

    @Bind(R.id.mv_code)
    MarkdownView mMvCode;
    @Bind(R.id.tv_result)
    TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_request_callback);
        ButterKnife.bind(this);

        setTitle("接口String回调");

        RequestParams params = new RequestParams(this);
        params.put("page", 1);
        params.put("limit", 12);
        HttpRequest.post(Api.NEW_GAME, params, new StringHttpRequestCallback() {
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                mTvResult.setText(JsonFormatUtils.formatJson(s));
            }
        });

        mMvCode.loadMarkdownFile("file:///android_asset/HttpRequestCallbackString.md", "file:///android_asset/css-themes/classic.css");
    }
}
