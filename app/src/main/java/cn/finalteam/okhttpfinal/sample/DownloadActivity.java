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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import java.io.File;
import us.feras.mdv.MarkdownView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/14 下午3:56
 */
public class DownloadActivity extends BaseActivity {

    @Bind(R.id.pb_download) ProgressBar mPbDownload;
    @Bind(R.id.btn_download) Button mBtnDownload;
    @Bind(R.id.mv_code) MarkdownView mMvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        setTitle("简单文件下载");

        mMvCode.loadMarkdownFile("file:///android_asset/Download.md", "file:///android_asset/css-themes/classic.css");
    }

    @OnClick(R.id.btn_download)
    public void download() {
        String url = "http://www.bus365.com/public/phoneClient/BUS365.apk";
        HttpRequest.download(url, new File("/sdcard/BUS365.apk"), new FileDownloadCallback() {
            @Override public void onStart() {
                super.onStart();
            }

            @Override
            public void onProgress(int progress, long networkSpeed) {
                super.onProgress(progress, networkSpeed);
                mPbDownload.setProgress(progress);
                //String speed = FileUtils.generateFileSize(networkSpeed);
            }

            @Override public void onFailure() {
                super.onFailure();
                Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override public void onDone() {
                super.onDone();
                Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
