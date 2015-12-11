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

import android.os.AsyncTask;
import cn.finalteam.toolsfinal.FileUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/10 下午10:45
 */
public class FileDownloadTask extends AsyncTask<Void, Integer, Boolean> {

    private OkHttpClient okHttpClient;
    private OkHttpFinal okHttpFinal;
    private FileDownloadCallback callback;
    private String url;
    private File target;

    public FileDownloadTask(String url, File target, FileDownloadCallback callback) {
        this.url = url;
        this.okHttpFinal = OkHttpFinal.getOkHttpFinal();
        this.okHttpClient = okHttpFinal.getOkHttpClient();
        this.callback = callback;
        this.target = target;

        FileUtils.makeFolders(target);
        if (target.exists()) {
            target.delete();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callback != null) {
            callback.onStart();
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //构造请求
        final Request request = new Request.Builder()
                .url(url)
                .build();

        boolean suc = false;
        try {
            Response response = okHttpClient.newCall(request).execute();
            long total = response.body().contentLength();
            saveFile(response);
            if (total == target.length()) {
                suc = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            suc = false;
        }

        return suc;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (callback != null) {
            int progress = values[0];
            callback.onProgress(progress);
        }
    }

    @Override
    protected void onPostExecute(Boolean suc) {
        super.onPostExecute(suc);
        if (suc) {
            if ( callback != null ) {
                callback.onDone();
            }
        } else {
            if ( callback != null ) {
                callback.onFailure();
            }
        }
    }

    public String saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            FileUtils.makeFolders(target);

            fos = new FileOutputStream(target);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);

                if (callback != null) {
                    publishProgress((int) (sum * 100.0f / total));
                }
            }
            fos.flush();

            return target.getAbsolutePath();
        } finally {
            try {
                if (is != null) { is.close(); }
            } catch (IOException e) {
            }
            try {
                if (fos != null) { fos.close(); }
            } catch (IOException e) {
            }
        }
    }
}
