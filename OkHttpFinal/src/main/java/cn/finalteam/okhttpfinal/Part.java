/*
 * Copyright (C) 2015 彭建波(pengjianbo@finalteam.cn), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.finalteam.okhttpfinal;

import android.text.TextUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/2/15 0015 14:57
 */
public final class Part {
    private String key;
    private String value;
    private FileWrapper fileWrapper;

    public Part(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public Part(String key, FileWrapper fileWrapper) {
        setKey(key);
        this.fileWrapper = fileWrapper;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public FileWrapper getFileWrapper() {
        return fileWrapper;
    }

    protected void setKey(String key) {
        if(key == null) {
            this.key = "";
        } else {
            this.key = key;
        }
    }

    protected void setValue(String value) {
        if(value == null) {
            this.value = "";
        } else {
            this.value = value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Part)){
            return false;
        }
        Part part = (Part) o;
        if (part == null){
            return false;
        }
        if (TextUtils.equals(part.getKey(), getKey()) && TextUtils.equals(part.getValue(), getValue())){
            return true;
        }
        return false;
    }
}
