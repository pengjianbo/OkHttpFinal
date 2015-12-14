#下载管理
## 添加下载

<pre>
String url = gameInfo.getUrl();
if (!DownloadManager.getInstance(this).hasTask(url)) {
	DownloadManager.getInstance(this).addTask(url, null);
}
</pre>
## 暂停下载

<pre>
DownloadManager.getInstance(this).stopTask(info.getUrl());
</pre>
## 继续下载

<pre>
DownloadManager.getInstance(this).restartTask(info.getUrl());
</pre>
## 添加事件回调

<pre>
DownloadManager.getInstance(this).addTaskListener(url, new DownloadListener() {
    @Override
    public void onProgress(DownloadInfo downloadInfo) {
        super.onProgress(downloadInfo);
        holder.mTvOperate.setText("暂停");
        holder.mTvDownloadState.setText("下载中");
        holder.mNumberProgressBar.setProgress(downloadInfo.getProgress());
        String downladScale = StringUtils.generateFileSize(downloadInfo.getDownloadLength()) + "/"
                + StringUtils.generateFileSize(downloadInfo.getTotalLength());
        holder.mTvDownloadScale.setText(downladScale);
        holder.mTvDownloadSpeed.setText(StringUtils.generateFileSize(downloadInfo.getNetworkSpeed()));
    }

    @Override
    public void onError(DownloadInfo downloadInfo) {
        super.onError(downloadInfo);
        holder.mTvOperate.setText("继续");
        holder.mTvDownloadState.setText("已暂停");
    }

    @Override
    public void onFinish(DownloadInfo downloadInfo) {
        super.onFinish(downloadInfo);
        holder.mTvDownloadState.setText("下载完成");
        holder.mTvOperate.setText("安装");
    }
});
</pre>
## 添加全局事件回调

<pre>
DownloadManager.getInstance(this).setGlobalDownloadListener(new DownloadListener());
</pre>
...