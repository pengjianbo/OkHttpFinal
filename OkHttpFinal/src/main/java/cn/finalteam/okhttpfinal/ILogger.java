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

import cn.finalteam.toolsfinal.logger.LoggerFactory;
import cn.finalteam.toolsfinal.logger.LoggerPrinter;
import cn.finalteam.toolsfinal.logger.Printer;
import cn.finalteam.toolsfinal.logger.Settings;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/2/2 0002 12:49
 */
class ILogger {
    public static final String DEFAULT_TAG = "OkHttpFinal";
    protected static boolean DEBUG = BuildConfig.DEBUG;
    private static LoggerPrinter printer;

    //no instance
    private ILogger() {
        printer = LoggerFactory.getFactory(DEFAULT_TAG, DEBUG);
    }

    private static void createInstance(){
        if (printer == null){
            new ILogger();
        }
    }

    public static void clear() {
        createInstance();
        printer.clear();
    }

    public static Settings getSettings() {
        createInstance();
        return printer.getSettings();
    }

    public static Printer t(String tag) {
        createInstance();
        return printer.t(tag, printer.getSettings().getMethodCount());
    }

    public static Printer t(int methodCount) {
        createInstance();
        return printer.t(null, methodCount);
    }

    public static Printer t(String tag, int methodCount) {
        createInstance();
        return printer.t(tag, methodCount);
    }

    public static void d(String message, Object... args) {
        createInstance();
        printer.d(message, args);
    }

    public static void e(Throwable throwable) {
        createInstance();
        printer.e(throwable);
    }

    public static void e(String message, Object... args) {
        createInstance();
        printer.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        createInstance();
        printer.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        createInstance();
        printer.i(message, args);
    }

    public static void v(String message, Object... args) {
        createInstance();
        printer.v(message, args);
    }

    public static void w(String message, Object... args) {
        createInstance();
        printer.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        createInstance();
        printer.wtf(message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        createInstance();
        printer.json(json);
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        createInstance();
        printer.xml(xml);
    }
}
