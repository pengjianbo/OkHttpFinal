package cn.finalteam.okhttpfinal.sample;

import android.os.Bundle;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.dm.DownloadInfo;
import cn.finalteam.okhttpfinal.dm.DownloadManager;
import cn.finalteam.okhttpfinal.sample.adapter.DownloadManagerListAdapter;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/29 下午5:43
 */
public class DownloadManangerActivity extends BaseActivity {

    @Bind(R.id.lv_task_list)
    ListView mLvTaskList;

    private DownloadManagerListAdapter mDownloadManagerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        ButterKnife.bind(this);

        setTitle("下载管理");
        List<DownloadInfo> list = DownloadManager.getInstance(this).getAllTask();
        mDownloadManagerListAdapter = new DownloadManagerListAdapter(this, list);
        mLvTaskList.setAdapter(mDownloadManagerListAdapter);
    }
}
