package cn.finalteam.okhttpfinal.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.okhttpfinal.sample.adapter.GameListAdapter;
import cn.finalteam.okhttpfinal.sample.http.model.UploadResponse;
import cn.finalteam.okhttpfinal.sample.model.GameInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Bind(R.id.lv_game)
    ListView mLvGame;
    @Bind(R.id.btn_upload) Button mBtnUpload;
    @Bind(R.id.btn_api_demo) Button mBtnApiDemo;
    @Bind(R.id.btn_dm) Button mBtnDm;
    @Bind(R.id.pb_upload) ProgressBar mPbUpload;
    @Bind(R.id.pb_download) ProgressBar mPbDownload;
    @Bind(R.id.btn_download) Button mBtnDownload;

    private List<GameInfo> mGameList = new ArrayList<>();
    private GameListAdapter mGameListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpRequest.setDebug(true);
        ButterKnife.bind(this);
        initImageLoader(this);

        mGameList.add(new GameInfo("冒险与挖矿", "com.speedsoftware.rootexplorer", "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware.rootexplorer_140220.apk", "http://anzhuo.webdown.paojiao.cn/game/05f/caf/ff63b7c3bc79c350437b96cce8/icon2.png"));
        mGameList.add(new GameInfo("二战风云", "com.wistone.war2victory", "http://anzhuo.webdown.paojiao.cn/game/6cb/ab7/cb92a447231d2865d6536b1b32/com.wistone.war2victory_20150909103114_2.8.1_build_16302_wst.apk", "http://anzhuo.webdown.paojiao.cn/game/6cb/ab7/cb92a447231d2865d6536b1b32/icon2.png"));
        mGameList.add(new GameInfo("开心消鱼儿", "com.cnnzzse.kxxye", "http://apk.r1.market.hiapk.com/data/upload/apkres/2015/9_23/17/com.cnnzzse.kxxye_050348.apk", "http://img.r1.market.hiapk.com/data/upload/2015/09_23/17/72_72_20150923050916_4223.png"));
        mGameList.add(new GameInfo("开心猜成语", "com.nerser.ccser", "http://apk.r1.market.hiapk.com/data/upload/apkres/2015/9_11/14/com.nerser.ccser_025042.apk", "http://img.r1.market.hiapk.com/data/upload/2015/09_11/14/72_72_20150911025051_7870.png"));
        mGameListAdapter = new GameListAdapter(this, mGameList);
        mLvGame.setAdapter(mGameListAdapter);


    }

    @OnClick(R.id.btn_api_demo)
    public void apiDemoAction() {
        startActivity(new Intent(this, NewGameListActivity.class));
    }

    @OnClick(R.id.btn_dm)
    public void dmAction() {
        startActivity(new Intent(this, DownloadManangerActivity.class));
    }

    @OnClick(R.id.btn_upload)
    public void upload() {
        File file = new File("/sdcard/DCIM/GalleryFinal/IMG20151201200821.jpg");
        String userId = "3097424";
        RequestParams params = new RequestParams(this);
        params.put("file", file);
        params.put("userId", userId);
        params.put("token", "NTCrWFKFCn1r8iaV3K0fLz2gX9LZS1SR");
        params.put("udid", "f0ba33e4de8a657d");
        params.put("sign", "39abfa9af6f6e3c8776b01ae612bc14c");
        params.put("version", "2.1.0");
        params.put("mac", "8c:3a:e3:5e:68:e0");
        params.put("appId", "paojiao_aiyouyou20");
        params.put("imei", "359250051610200");
        params.put("model", "Nexus 5");
        params.put("cid", "paojiao");
        String fileuploadUri = "http://uploader.paojiao.cn/avatarAppUploader?userId=" + userId;

        HttpRequest.post(fileuploadUri, params, new BaseHttpRequestCallback<UploadResponse>() {
            @Override
            public void onSuccess(UploadResponse uploadResponse) {
                super.onSuccess(uploadResponse);
                Toast.makeText(getBaseContext(), "上传成功：" + uploadResponse.getData(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(getBaseContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress, long currentLength, long totalLength, boolean done) {
                mPbUpload.setProgress(progress);
            }
        });
    }

    @OnClick(R.id.btn_download)
    public void download() {
        String url = "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware.rootexplorer_140220.apk";
        HttpRequest.download(url, new File("/sdcard/rootexplorer_140220.apk"), new FileDownloadCallback() {
            @Override public void onStart() {
                super.onStart();
            }

            @Override
            public void onProgress(int progress) {
                super.onProgress(progress);
                mPbDownload.setProgress(progress);
            }

            @Override public void onFailure() {
                super.onFailure();
                Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override public void onDone() {
                super.onDone();
                Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
            }
        });
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
