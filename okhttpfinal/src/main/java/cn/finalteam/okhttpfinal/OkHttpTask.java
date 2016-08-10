package cn.finalteam.okhttpfinal;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

import cn.finalteam.toolsfinal.JsonFormatUtils;
import cn.finalteam.toolsfinal.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/4/19 上午10:24
 */
class OkHttpTask implements Callback, ProgressCallback{

    private Handler handler = new Handler(Looper.getMainLooper());
    public static final String DEFAULT_HTTP_TASK_KEY = "default_http_task_key";

    private String url;
    private RequestParams params;
    private BaseHttpRequestCallback callback;
    private Headers headers;
    private String requestKey;
    private Method method;
    private OkHttpClient okHttpClient;

    public OkHttpTask(Method method, String url, RequestParams params, OkHttpClient.Builder builder, BaseHttpRequestCallback callback) {
        this.method = method;
        this.url = url;
        this.callback = callback;
        if (params == null) {
            this.params = new RequestParams();
        } else {
            this.params = params;
        }
        this.requestKey = this.params.getHttpTaskKey();
        if (StringUtils.isEmpty(requestKey)) {
            requestKey = DEFAULT_HTTP_TASK_KEY;
        }

        //将请求的URL及参数组合成一个唯一请求
        HttpTaskHandler.getInstance().addTask(this.requestKey, this);
        okHttpClient = builder.build();
    }

    public String getUrl() {
        return url;
    }

    protected void execute() {
        if (params.headers != null) {
            headers = params.headers.build();
        }

        if (callback != null) {
            callback.onStart();
        }

        try {
            run();
        } catch (Exception e) {
            ILogger.e(e);
        }
    }

    protected void run() throws Exception{
        String srcUrl = url;
        //构建请求Request实例
        Request.Builder builder = new Request.Builder();

        switch (method) {
            case GET:
                url = Utils.getFullUrl(url, params.getFormParams(), params.isUrlEncoder());
                builder.get();
                break;
            case DELETE:
                url = Utils.getFullUrl(url, params.getFormParams(), params.isUrlEncoder());
                builder.delete();
                break;
            case HEAD:
                url = Utils.getFullUrl(url, params.getFormParams(), params.isUrlEncoder());
                builder.head();
                break;
            case POST:
                RequestBody body = params.getRequestBody();
                if (body != null) {
                    builder.post(new ProgressRequestBody(body, this));
                }
                break;
            case PUT:
                RequestBody bodyPut = params.getRequestBody();
                if (bodyPut != null) {
                    builder.put(new ProgressRequestBody(bodyPut, this));
                }
                break;
            case PATCH:
                RequestBody bodyPatch = params.getRequestBody();
                if (bodyPatch != null) {
                    builder.put(new ProgressRequestBody(bodyPatch, this));
                }
                break;
        }
        if (params.cacheControl != null) {
            builder.cacheControl(params.cacheControl);
        }
        builder.url(url).tag(srcUrl).headers(headers);
        Request request = builder.build();
        if (Constants.DEBUG) {
            ILogger.d("url=" + srcUrl + "?" + params.toString() + "\n header=" + headers.toString());
        }
        Call call = okHttpClient.newCall(request);
        OkHttpCallManager.getInstance().addCall(url, call);
        //执行请求
        call.enqueue(this);
    }
    public void cancel(){
        //防止内存泄漏
        HttpRequest.cancel(url);
        this.callback=null;
        if(params!=null){
            this.params.httpCycleContext=null;
            this.params=null;
        }
    }
    /**
     * 处理进度
     * @param progress
     * @param networkSpeed
     * @param done
     */
    @Override
    public void updateProgress(final int progress, final long networkSpeed, final boolean done) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onProgress(progress, networkSpeed, done);
                }
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
        ResponseData responseData = new ResponseData();
        if (e instanceof SocketTimeoutException) {
            responseData.setTimeout(true);
        } else if (e instanceof InterruptedIOException && TextUtils.equals(e.getMessage(), "timeout")) {
            responseData.setTimeout(true);
        }

        handlerResponse(responseData, null);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        ResponseData responseData = new ResponseData();
        handlerResponse(responseData, response);
    }

    private void handlerResponse(final ResponseData responseData, Response response) {
        //获取请求结果
        if (response != null) {
            responseData.setResponseNull(false);
            responseData.setCode(response.code());
            responseData.setMessage(response.message());
            responseData.setSuccess(response.isSuccessful());
            String respBody = "";
            try {
                respBody = response.body().string();
            } catch (IOException e) {
                ILogger.e(e);
            }
            responseData.setResponse(respBody);
            responseData.setHeaders(response.headers());
        } else {
            responseData.setResponseNull(true);
            responseData.setCode(BaseHttpRequestCallback.ERROR_RESPONSE_UNKNOWN);
            if(responseData.isTimeout()) {
                responseData.setMessage("request timeout");
            } else {
                responseData.setMessage("http exception");
            }
        }
        responseData.setHttpResponse(response);

        //跳转到主线程
        handler.post(new Runnable() {
            @Override
            public void run() {
                onPostExecute(responseData);
            }
        });
    }

    protected void onPostExecute(ResponseData responseData) {
        OkHttpCallManager.getInstance().removeCall(url);
        //判断请求是否在这个集合中
        if (!HttpTaskHandler.getInstance().contains(requestKey)) {
            return;
        }

        if (callback != null) {
            callback.setResponseHeaders(responseData.getHeaders());
            callback.onResponse(responseData.getHttpResponse(), responseData.getResponse(), responseData.getHeaders());
            callback.onResponse(responseData.getResponse(), responseData.getHeaders());
        }

        int code = responseData.getCode();
        String msg = responseData.getMessage();

        if (!responseData.isResponseNull()) {//请求得到响应
            if (responseData.isSuccess()) {//成功的请求
                String respBody = responseData.getResponse();
                if (Constants.DEBUG) {
                    Headers headers = responseData.getHeaders();
                    String respHeader = "";
                    if (headers != null) {
                        respHeader = headers.toString();
                    }
                    ILogger.d("url=" + url + "\n result=" + JsonFormatUtils.formatJson(respBody) + "\n header=" + respHeader);
                }
                parseResponseBody(responseData, callback);
            } else {//请求失败
                if (Constants.DEBUG) {
                    ILogger.d("url=" + url + "\n response failure code=" + code + " msg=" + msg);
                }

                if (callback != null) {
                    callback.onFailure(code, msg);
                }
            }
        } else {
            if (Constants.DEBUG) {
                ILogger.d("url=" + url + "\n response failure code=" + code + " msg=" + msg);
            }
            if (callback != null) {
                callback.onFailure(code, msg);
            }
        }

        if (callback != null) {
            callback.onFinish();
        }
    }

    /**
     * 解析响应数据
     *
     * @param responseData 请求的response
     * @param callback     请求回调
     */
    private void parseResponseBody(ResponseData responseData, BaseHttpRequestCallback callback) {
        //回调为空，不向下执行
        if (callback == null) {
            return;
        }

        String result = responseData.getResponse();

        if (StringUtils.isEmpty(result)) {
            ILogger.e("response empty!!!");
        }

        if (callback.type == String.class) {
            callback.onSuccess(responseData.getHeaders(), result);
            callback.onSuccess(result);
            return;
        } else if (callback.type == JSONObject.class) {
            JSONObject jsonObject = null;
            try {
                jsonObject = JSON.parseObject(result);
            } catch (Exception e) {
                ILogger.e(e);
            }
            if (jsonObject != null) {
                callback.onSuccess(responseData.getHeaders(), jsonObject);
                callback.onSuccess(jsonObject);
                return;
            }
        } else if (callback.type == JSONArray.class) {
            JSONArray jsonArray = null;
            try {
                jsonArray = JSON.parseArray(result);
            } catch (Exception e) {
                ILogger.e(e);
            }

            if (jsonArray != null) {
                callback.onSuccess(responseData.getHeaders(), jsonArray);
                callback.onSuccess(jsonArray);
                return;
            }
        } else {
            Object obj = null;
            try {
                obj = JSON.parseObject(result, callback.type);
            } catch (Exception e) {
                ILogger.e(e);
            }
            if (obj != null) {
                callback.onSuccess(responseData.getHeaders(), obj);
                callback.onSuccess(obj);
                return;
            }
        }
        //接口请求失败
        callback.onFailure(BaseHttpRequestCallback.ERROR_RESPONSE_DATA_PARSE_EXCEPTION, "Data parse exception");
    }
}
