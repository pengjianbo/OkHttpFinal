package cn.finalteam.okhttpfinal.sample.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.dm.FileDownloadListener;
import cn.finalteam.okhttpfinal.dm.DownloadManager;
import cn.finalteam.okhttpfinal.dm.db.FileDownloadInfo;
import cn.finalteam.okhttpfinal.sample.Constants;
import cn.finalteam.okhttpfinal.sample.R;
import cn.finalteam.okhttpfinal.sample.http.MyHttpCycleContext;
import cn.finalteam.okhttpfinal.sample.model.GameDownloadInfo;
import cn.finalteam.toolsfinal.ApkUtils;
import cn.finalteam.toolsfinal.AppCacheUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.coder.Base64Coder;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/8 上午9:26
 */
public class DownloadManagerListAdapter extends CommonBaseAdapter<DownloadManagerListAdapter.FileViewHolder, FileDownloadInfo> {

    private View mLastShowBottomBar;
    private int mLastShowBottomBarPos = -1;
    private Map<String, MyDLTaskListener> mDListenerMap;

    public DownloadManagerListAdapter(MyHttpCycleContext httpCycleContext, List<FileDownloadInfo> list) {
        super(httpCycleContext, list);
        this.mDListenerMap = new HashMap<>();
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.adapter_download_manager_list_item, parent);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        final FileDownloadInfo info = mList.get(position);
        holder.mNumberProgressBar.setProgress(info.getProgress());
        if ( info.getTotalLength() > 0 ) {
            String downladScale = FileUtils.generateFileSize(info.getDownloadLength()) + "/"
                    + FileUtils.generateFileSize(info.getTotalLength());
            holder.mTvDownloadScale.setText(downladScale);
        }
        if ( info.getState() == DownloadInfo.DOWNLOADING || info.getState() == DownloadInfo.WAIT) {
            holder.mBtnOperate.setText("暂停");
            if ( info.getState() == DownloadInfo.WAIT ) {
                holder.mTvDownloadState.setText("等待下载");
            } else {
                holder.mTvDownloadState.setText("下载中");
            }
        } else if ( info.getState() == DownloadInfo.COMPLETE ) {
            holder.mBtnOperate.setText("安装");
            holder.mTvDownloadState.setText("下载完成");
        } else {
            holder.mBtnOperate.setText("继续");
            holder.mTvDownloadState.setText("已暂停");
        }

        if (mLastShowBottomBarPos == position) {
            holder.mLlBottomBar.setVisibility(View.VISIBLE);
        } else {
            holder.mLlBottomBar.setVisibility(View.GONE);
        }

        String key = String.format(Constants.GAME_DOWNLOAD_INFO, Base64Coder.encodeToString(info.getUrl().getBytes(), Base64Coder.DEFAULT));
        GameDownloadInfo gameDownloadInfo = (GameDownloadInfo) AppCacheUtils.getInstance(mHttpCycleContext.getContext()).getObject(key);
        if ( gameDownloadInfo != null ) {
            holder.mTvTitle.setText(gameDownloadInfo.getAppName());
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

            ImageLoader.getInstance().displayImage(gameDownloadInfo.getLogo(), holder.mIvIcon, options);
        }
        holder.view.setOnClickListener(new ItemClickListener(holder, position));
        holder.mTvCancel.setOnClickListener(new CancelClickListener(info));
        holder.mBtnOperate.setOnClickListener(new OperateButtonClickListener(info, holder));
        holder.mTvGameDetail.setOnClickListener(new GameDetailClickListener(info));

        addDownloadListener(info, holder);
    }

    /**
     * 下载进度 listener
     */
    private class MyDLTaskListener extends FileDownloadListener {
        private FileViewHolder holder;
        public MyDLTaskListener(FileViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onProgress(FileDownloadInfo downloadInfo) {
            super.onProgress(downloadInfo);
            holder.mBtnOperate.setText("暂停");
            holder.mTvDownloadState.setText("下载中");
            holder.mNumberProgressBar.setProgress(downloadInfo.getProgress());
            String downladScale = FileUtils.generateFileSize(downloadInfo.getDownloadLength()) + "/"
                    + FileUtils.generateFileSize(downloadInfo.getTotalLength());
            holder.mTvDownloadScale.setText(downladScale);
            holder.mTvDownloadSpeed.setText(FileUtils.generateFileSize(downloadInfo.getNetworkSpeed()));
        }

        @Override
        public void onError(FileDownloadInfo downloadInfo) {
            super.onError(downloadInfo);
            holder.mBtnOperate.setText("继续");
            holder.mTvDownloadState.setText("已暂停");
            notifyDataSetChanged();
        }

        @Override
        public void onFinish(FileDownloadInfo downloadInfo) {
            super.onFinish(downloadInfo);
            holder.mTvDownloadState.setText("下载完成");
            holder.mBtnOperate.setText("安装");
            notifyDataSetChanged();
        }
    }

    /**
     * 继续、停止、安装按钮事件
     */
    private class OperateButtonClickListener implements View.OnClickListener {

        private FileDownloadInfo info;
        private FileViewHolder holder;

        public OperateButtonClickListener(FileDownloadInfo info, FileViewHolder holder) {
            this.info = info;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            int state = info.getState();
            if ( state == DownloadInfo.DOWNLOADING || state == DownloadInfo.WAIT) {
                Logger.d("DownloadInfo.DOWNLOADING ");
                DownloadManager.getInstance(mContext).stopTask(info.getUrl());
                holder.mBtnOperate.setText("继续");

            } else if ( state == DownloadInfo.COMPLETE ) {
                Logger.d("DownloadInfo.COMPLETE ");
                holder.mTvDownloadState.setText("下载完成");
                holder.mBtnOperate.setText("安装");
                ApkUtils.install(mContext, new File(info.getTargetPath()));
            } else {
                DownloadManager.getInstance(mContext).restartTask(info.getUrl());
                holder.mBtnOperate.setText("暂停");
                if ( DownloadManager.getInstance(mContext).getDownloadingSize() >= 3 ) {
                    holder.mTvDownloadState.setText("等待下载");
                } else {
                    holder.mTvDownloadState.setText("下载中");
                }
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 取消按钮
     */
    private class CancelClickListener implements View.OnClickListener {

        private DownloadInfo info;

        public CancelClickListener(DownloadInfo info) {
            this.info = info;
        }

        @Override
        public void onClick(View view) {
            mList.remove(info);
            mLastShowBottomBarPos = -1;
            notifyDataSetChanged();
            DownloadManager.getInstance(mContext).deleteTask(info.getUrl());
        }
    }

    private class GameDetailClickListener implements View.OnClickListener {

        private DownloadInfo info;

        public GameDetailClickListener(DownloadInfo info) {
            this.info = info;
        }

        @Override
        public void onClick(View view) {
        }
    }

    /**
     * Item click listener
     */
    private class ItemClickListener implements View.OnClickListener {

        private FileViewHolder holder;
        private int position;

        public ItemClickListener(FileViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View view) {

            //隐藏上一个BottomBar
            if (mLastShowBottomBar != null && mLastShowBottomBar != holder.mLlBottomBar) {
                mLastShowBottomBar.setVisibility(View.GONE);
            }

            if (holder.mLlBottomBar.getVisibility() == View.VISIBLE) {
                holder.mLlBottomBar.setVisibility(View.GONE);
                mLastShowBottomBarPos = -1;
            } else {
                holder.mLlBottomBar.setVisibility(View.VISIBLE);
                mLastShowBottomBarPos = position;
                mLastShowBottomBar = holder.mLlBottomBar;
            }

            notifyDataSetChanged();
        }
    }

    /**
     * 添加下载回调
     * @param info
     * @param holder
     */
    private void addDownloadListener(DownloadInfo info, FileViewHolder holder) {
        String key = info.getUrl();
        MyDLTaskListener dlListener = mDListenerMap.get(key);
        if (dlListener == null) {
            MyDLTaskListener listener = new MyDLTaskListener(holder);
            mDListenerMap.put(key, listener);
            DownloadManager.getInstance(mContext).addTaskListener(info.getUrl(), listener);
        }
    }


    static class FileViewHolder extends CommonBaseAdapter.ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView mIvIcon;
        @Bind(R.id.number_progress_bar)
        ProgressBar mNumberProgressBar;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.btn_operate)
        Button mBtnOperate;
        @Bind(R.id.tv_download_scale) TextView mTvDownloadScale;
        @Bind(R.id.tv_download_state) TextView mTvDownloadState;
        @Bind(R.id.tv_game_detail) TextView mTvGameDetail;
        @Bind(R.id.tv_cancel) TextView mTvCancel;
        @Bind(R.id.ll_bottom_bar)
        RelativeLayout mLlBottomBar;
        @Bind(R.id.tv_download_speed) TextView mTvDownloadSpeed;
        public FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
