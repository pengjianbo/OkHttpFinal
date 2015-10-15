package cn.finalteam.okhttpfinal;

import cn.finalteam.toolsfinal.StringUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/**
 * Desction:Http请求参数类
 * Author:pengjianbo
 * Date:15/7/3 上午11:05
 */
public class RequestParams {
    protected ConcurrentHashMap<String, String> headerMap;

    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, FileWrapper> fileParams;

    protected HttpCycleContext httpCycleContext;
    private String httpTaskKey;
    private boolean jsonBody;

    public RequestParams() {
        this(null);
    }

    public RequestParams(HttpCycleContext cycleContext) {
        this(cycleContext, false);
    }

    public RequestParams(HttpCycleContext cycleContext, boolean jsonBody) {
        this.httpCycleContext = cycleContext;
        this.jsonBody = jsonBody;
        init();
    }

    private void init() {
        headerMap = new ConcurrentHashMap<>();
        urlParams = new ConcurrentHashMap<>();
        fileParams = new ConcurrentHashMap<>();

        headerMap.put("charset", "UTF-8");

        if ( httpCycleContext != null ) {
            httpTaskKey = httpCycleContext.getHttpTaskKey();
        }
    }

    public String getHttpTaskKey() {
        return this.httpTaskKey;
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if ( value == null ) {
            value = "";
        }

        if (!StringUtils.isEmpty(key)) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, int value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, float value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, double value) {
        put(key, String.valueOf(value));
    }

    public void put(String key, boolean value) {
        put(key, String.valueOf(value));
    }
    /**
     * @param key
     * @param file
     */
    public void put(String key, File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }

        try {
            boolean isPng = file.getName().lastIndexOf("png") > 0 || file.getName().lastIndexOf("PNG") > 0;
            if (isPng) {
                put(key, new HttpFileInputStream(new FileInputStream(file), file.getName(), file.length()), ContentType.PNG.getContentType());
            }

            boolean isJpg = file.getName().lastIndexOf("jpg") > 0 || file.getName().lastIndexOf("JPG") > 0;
            if (isJpg) {
                put(key, new HttpFileInputStream(new FileInputStream(file), file.getName(), file.length()), ContentType.JPEG.getContentType());
            }

            if (!isPng && !isJpg) {
                put(key, new HttpFileInputStream(new FileInputStream(file), file.getName(), file.length()), ContentType.TEXT.getContentType());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, HttpFileInputStream httpFileInputStream) {
        put(key, httpFileInputStream, ContentType.PNG.getContentType());
    }

    public void put(String key, HttpFileInputStream stream, String contentType) {
        if (!StringUtils.isEmpty(key) && stream != null) {
            fileParams.put(key, new FileWrapper(stream.getInputStream(), stream.getName(), contentType, stream.getFileSize()));
        }
    }

    public void putAll(Map<String, String> params) {
        if ( params != null && params.size() > 0 ) {
            urlParams.putAll(params);
        }
    }

    public void putHeader(String key, String value) {
        if ( value == null ) {
            value = "";
        }

        if (!StringUtils.isEmpty(key)) {
            headerMap.put(key, value);
        }
    }

    public void putHeader(String key, int value) {
        put(key, String.valueOf(value));
    }

    public void putHeader(String key, float value) {
        put(key, String.valueOf(value));
    }

    public void putHeader(String key, double value) {
        put(key, String.valueOf(value));
    }

    public void putHeader(String key, boolean value) {
        put(key, String.valueOf(value));
    }

    private static class FileWrapper {
        public InputStream inputStream;
        public String fileName;
        public String contentType;
        private long fileSize;

        public FileWrapper(InputStream inputStream, String fileName, String contentType, long fileSize) {
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.contentType = contentType;
            this.fileSize = fileSize;
        }

        public String getFileName() {
            if (fileName != null) {
                return fileName;
            } else {
                return "nofilename";
            }
        }
    }

    public void clearMap() {
        urlParams.clear();
        fileParams.clear();
    }

    public String toJSON() {
        String rsStr = new JSONObject(urlParams).toString();
        return rsStr;
    }

    public Map<String, String> getUrlParams() {
        return urlParams;
    }

    public RequestBody getRequestBody() {
        RequestBody body = null;
        if (fileParams.size() > 0 || jsonBody) {
            boolean hasData = false;

            MultipartBuilder builder = new MultipartBuilder();
            builder.type(MultipartBuilder.FORM);
            for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
                hasData = true;
            }

            for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
                FileWrapper file = entry.getValue();
                if (file.inputStream != null) {
                    hasData = true;
                    builder.addFormDataPart(entry.getKey(), file.getFileName(),
                            new IORequestBody(file.contentType, file.fileSize, file.inputStream));
                }
            }
            if (hasData) {
                body = builder.build();
            }
        } else {
            FormEncodingBuilder builder = new FormEncodingBuilder();
            boolean hasData = false;
            for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
                hasData = true;
            }
            if (hasData) {
                body = builder.build();
            }
        }

        return body;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        return result.toString();
    }
}
