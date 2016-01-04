package cn.finalteam.okhttpfinal.sample.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.dm.DownloadListener;
import cn.finalteam.okhttpfinal.dm.DownloadManager;
import cn.finalteam.okhttpfinal.sample.Constants;
import cn.finalteam.okhttpfinal.sample.R;
import cn.finalteam.okhttpfinal.sample.http.MyHttpCycleContext;
import cn.finalteam.okhttpfinal.sample.model.GameDownloadInfo;
import cn.finalteam.okhttpfinal.sample.model.GameInfo;
import cn.finalteam.toolsfinal.AppCacheUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.coder.Base64Coder;

import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/8 下午11:53
 */
public class GameListAdapter extends CommonBaseAdapter<GameListAdapter.GameListHolder, GameInfo> {

    public GameListAdapter(MyHttpCycleContext context, List<GameInfo> list) {
        super(context, list);
    }

    @Override
    public GameListHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = mInflater.inflate(R.layout.adapter_game_list_item, null);
        return new GameListHolder(view);
    }

    @Override
    public void onBindViewHolder(final GameListHolder holder, int position) {
        final GameInfo gameInfo = mList.get(position);
        holder.mTvGameName.setText(gameInfo.getGameName());
        DownloadManager.getInstance().addTask(gameInfo.getUrl(), new DownloadListener() {
            @Override
            public void onStart(BaseDownloadTask task, int preProgress) {
                super.onStart(task, preProgress);
                Logger.i("=======onStar" + preProgress);
            }

            @Override
            public void onProgress(BaseDownloadTask task, int preProgress) {
                super.onProgress(task, preProgress);
                Logger.i("=======onProgress" + preProgress);
            }

            @Override
            public void onStop(BaseDownloadTask task) {
                super.onStop(task);
                Logger.i("=======onStop");
            }

            @Override
            public void onFinish(BaseDownloadTask task) {
                super.onFinish(task);
                Logger.i("=======onFinish=" + task.getPath());
            }
        });
//        if ( DownloadManager.getInstance().hasTask(gameInfo.getUrl()) ) {
//            holder.mBtnDownload.setEnabled(false);
//            holder.mBtnDownload.setText("已在队列");
//        }
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.mipmap.ic_launcher)
//                .showImageOnFail(R.mipmap.ic_launcher)
//                .showImageOnLoading(R.mipmap.ic_launcher)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//        ImageLoader.getInstance().displayImage(gameInfo.getCoverUrl(), holder.mIvGameIcon, options);
//        holder.mBtnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = gameInfo.getUrl();
//                if (!DownloadManager.getInstance().hasTask(url)) {
//                    DownloadManager.getInstance().startTask(url, null);
//
//                    holder.mBtnDownload.setEnabled(false);
//                    holder.mBtnDownload.setText("已在队列");
//
//                    GameDownloadInfo info = new GameDownloadInfo();
//                    info.setAppName(gameInfo.getGameName());
//                    info.setLogo(gameInfo.getCoverUrl());
//                    info.setPackageName(gameInfo.getPackageName());
//                    String key = String.format(Constants.GAME_DOWNLOAD_INFO, Base64Coder.encodeToString(url.getBytes(), Base64Coder.DEFAULT));
//                    AppCacheUtils.getInstance(mContext).put(key, info);
//                }
//            }
//        });
    }

    static class GameListHolder extends CommonBaseAdapter.ViewHolder {
        @Bind(R.id.iv_game_icon)
        ImageView mIvGameIcon;
        @Bind(R.id.tv_game_name)
        TextView mTvGameName;
        @Bind(R.id.btn_download)
        Button mBtnDownload;

        public GameListHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
