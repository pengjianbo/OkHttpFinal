package cn.finalteam.okhttpfinal.sample.http;

import android.content.Context;
import cn.finalteam.okhttpfinal.HttpCycleContext;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/26 下午6:05
 */
public interface MyHttpCycleContext extends HttpCycleContext{
     Context getContext();
}
