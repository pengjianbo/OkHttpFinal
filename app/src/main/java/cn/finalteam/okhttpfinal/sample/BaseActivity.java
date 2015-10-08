package cn.finalteam.okhttpfinal.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import cn.finalteam.okhttpfinal.HttpTaskHandler;
import cn.finalteam.okhttpfinal.sample.http.MyHttpCycleContext;
import cn.finalteam.toolsfinal.DeviceUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/26 下午5:59
 */
public class BaseActivity extends AppCompatActivity implements MyHttpCycleContext {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = DeviceUtils.getScreenPix(this);
        Global.SCREEN_WIDTH = dm.widthPixels;
        Global.SCREEN_HEIGHT = dm.heightPixels;
    }

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
}
