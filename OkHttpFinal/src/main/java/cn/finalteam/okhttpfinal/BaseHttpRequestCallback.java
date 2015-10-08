package cn.finalteam.okhttpfinal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Desction:请求回调类
 * Author:pengjianbo
 * Date:15/7/3 上午11:41
 */
public class BaseHttpRequestCallback<T extends ApiResponse> {

    public static final int ERROR_RESPONSE_NULL = 1001;
    public static final int ERROR_RESPONSE_JSON_EXCEPTION = 1002;
    public static final int ERROR_RESPONSE_UNKNOWN = 1003;
    public static final int ERROR_RESPONSE_TIMEOUT = 1004;

    public BaseHttpRequestCallback() {
    }

    public void onStart() {
    }

    public void onFinish() {
    }

    public void onSuccess(T t) {
    }

    public void onFailure(int errorCode, String msg) {
    }

    public Class getModelClazz() {
        return getGenericType(0);
    }

    private Class getGenericType(int index) {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}
