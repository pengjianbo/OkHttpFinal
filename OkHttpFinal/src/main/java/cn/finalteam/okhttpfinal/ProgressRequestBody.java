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

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Desction:包装的请求体，处理进度
 * Author:pengjianbo
 * Date:15/12/10 下午5:31
 */
class ProgressRequestBody extends RequestBody {
    //开始时间，用户计算加载速度
    private long previousTime;

    protected RequestBody delegate;
    protected ProgressCallback callback;

    protected CountingSink countingSink;

    public ProgressRequestBody(RequestBody delegate, ProgressCallback callback) {
        this.delegate = delegate;
        this.callback = callback;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        previousTime = System.currentTimeMillis();
        countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);

        delegate.writeTo(bufferedSink);

        bufferedSink.flush();

    }

    protected final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;
        //总字节长度，避免多次调用contentLength()方法
        long contentLength = 0L;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            if (contentLength == 0) {
                //获得contentLength的值，后续不再调用
                contentLength = contentLength();
            }
            bytesWritten += byteCount;
            //回调
            if (callback != null) {
                //计算速度
                long totalTime = (System.currentTimeMillis() - previousTime) / 1000;
                if (totalTime == 0) {
                    totalTime += 1;
                }
                long networkSpeed = bytesWritten / totalTime;
                int progress = (int) (bytesWritten * 100 / contentLength);
                boolean done = bytesWritten == contentLength;
                callback.updateProgress(progress, networkSpeed, done);
            }
        }

    }
}
