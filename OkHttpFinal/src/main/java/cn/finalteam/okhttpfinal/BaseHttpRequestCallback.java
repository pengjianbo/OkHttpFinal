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
