package cn.finalteam.okhttpfinal.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initImageLoader(this);
    }

    @OnClick(R.id.btn_api_bean)
    public void apiDemoAction() {
        startActivity(new Intent(this, NewGameListActivity.class));
    }

    @OnClick(R.id.btn_upload)
    public void upload() {
        startActivity(new Intent(this, UploadActivity.class));
    }

    @OnClick(R.id.btn_api_string)
    public void apiString() {
        startActivity(new Intent(this, HttpRequestCallbackStringActivity.class));
    }

    @OnClick(R.id.btn_dm)
    public void downloadManager() {
        Uri uri = Uri.parse("https://github.com/pengjianbo/FileDownloaderFinal");
        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.btn_download)
    public void simpleDownload() {
        startActivity(new Intent(this, DownloadActivity.class));
    }

    @OnClick(R.id.btn_api_jsonobject)
    public void apiJsonObject() {
        startActivity(new Intent(this, HttpRequestCallbackJsonActivity.class));
    }

    @OnClick(R.id.btn_other_funcation)
    public void otherFunction() {
        startActivity(new Intent(this, OtherFuncationActivity.class));
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
