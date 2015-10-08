package cn.finalteam.okhttpfinal;

import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;

/**
 * Desction:生产http client
 * Author:pengjianbo
 * Date:15/7/3 下午6:13
 */
public class OkHttpFactory {

    public static OkHttpClient getOkHttpClientFactory(int timeout) {
        OkHttpClient client = new OkHttpClient();
        //设置请求时间
        client.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(timeout, TimeUnit.MILLISECONDS);
        client.setReadTimeout(timeout, TimeUnit.MILLISECONDS);
        //请求不重复
        client.setRetryOnConnectionFailure(false);
        //请求支持重定向
        client.setFollowRedirects(true);

        return client;
    }
}
