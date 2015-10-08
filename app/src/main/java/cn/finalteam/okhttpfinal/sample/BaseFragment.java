package cn.finalteam.okhttpfinal.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import cn.finalteam.okhttpfinal.HttpTaskHandler;
import cn.finalteam.okhttpfinal.sample.http.MyHttpCycleContext;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/26 下午7:26
 */
public class BaseFragment extends Fragment implements MyHttpCycleContext{

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
}
