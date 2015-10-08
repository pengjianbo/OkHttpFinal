package cn.finalteam.okhttpfinal;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Desction:上传文件数据模型
 * Author:pengjianbo
 * Date:15/7/3 上午11:10
 */
public class HttpFileInputStream implements Serializable {
    private static final long serialVersionUID = 1L;
    private InputStream inputStream;
    private String name;
    private long fileSize;

    public HttpFileInputStream(InputStream inputStream, String name, long fileSize) {
        this.inputStream = inputStream;
        this.name = name;
        this.fileSize = fileSize;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
