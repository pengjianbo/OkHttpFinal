package cn.finalteam.okhttpfinal;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.io.InputStream;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Desction:上传文件IORequest
 * Author:pengjianbo
 * Date:15/7/3 上午11:07
 */
public class IORequestBody extends RequestBody {

    private String contentType;
    private long fileSize;
    private InputStream inputStream;

    public IORequestBody(String contentType, long fileSize, InputStream inputStream) {
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.inputStream = inputStream;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public long contentLength() {
        return fileSize;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if ( this.inputStream != null ) {
            Source source = null;
            try {
                source = Okio.source(inputStream);
                sink.writeAll(source);
            } finally {
                Util.closeQuietly(source);
            }
        }
    }
}