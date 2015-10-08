package cn.finalteam.okhttpfinal.sample;

import android.os.Bundle;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/29 下午5:43
 */
public class DownloadManangerActivity extends BaseActivity {

    @Bind(R.id.lv_task_list)
    ListView mLvTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        ButterKnife.bind(this);

    }
}
