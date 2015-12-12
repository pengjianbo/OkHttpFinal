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

import java.util.Iterator;
import java.util.Map;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/11 下午11:58
 */
class Utils {

    public static String getFullUrl(String url, Map<String, String> paramsMap) {
        StringBuffer urlFull = new StringBuffer();
        urlFull.append(url);
        if (urlFull.indexOf("?", 0) < 0 && paramsMap.size() > 0) {
            urlFull.append("?");
        }
        Iterator<Map.Entry<String, String>> paramsIterator = paramsMap.entrySet().iterator();
        while (paramsIterator.hasNext()) {
            Map.Entry<String, String> entry = paramsIterator.next();
            String key = entry.getKey();
            String value = entry.getValue();

            urlFull.append(key).append("=").append(value);
            if (paramsIterator.hasNext()) {
                urlFull.append("&");
            }
        }
        url = urlFull.toString();
        return url;
    }
}
