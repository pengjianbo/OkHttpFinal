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
 * Desction:
 * Author:pengjianbo
 * Date:15/12/12 下午7:07
 */
 class ClassTypeReflect {


    //public static Type getSuperclassTypeParameter(Class<?> subclass){
    //    Type superclass = subclass.getGenericSuperclass();
    //    if (superclass instanceof Class){
    //        throw new RuntimeException("Missing type parameter.");
    //    }
    //    ParameterizedType parameterized = (ParameterizedType) superclass;
    //    return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    //}

    static Type getModelClazz(Class<?> subclass) {
        return getGenericType(0, subclass);
    }

    private static Type getGenericType(int index, Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) superclass).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return params[index];
    }
}
