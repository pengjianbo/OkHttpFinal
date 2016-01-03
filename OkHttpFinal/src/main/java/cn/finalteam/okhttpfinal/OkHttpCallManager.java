package cn.finalteam.okhttpfinal;

import cn.finalteam.toolsfinal.StringUtils;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Call;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/1/3 下午1:27
 */
public class OkHttpCallManager {

    private ConcurrentHashMap<String, Call> mCallMap;
    private static OkHttpCallManager mManager;

    private OkHttpCallManager() {
        mCallMap = new ConcurrentHashMap<>();
    }

    public static OkHttpCallManager getInstance() {
        if (mManager == null) {
            mManager = new OkHttpCallManager();
        }
        return mManager;
    }

    public void addCall(String url, Call call) {
        if (call != null && StringUtils.isEmpty(url)) {
            mCallMap.put(url, call);
        }
    }

    public Call getCall(String url) {
        if ( StringUtils.isEmpty(url) ) {
            return mCallMap.get(url);
        }

        return null;
    }

    public void removeCall(String url) {
        if ( StringUtils.isEmpty(url) ) {
            mCallMap.remove(url);
        }
    }

}
