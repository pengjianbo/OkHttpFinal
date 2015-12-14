##简单的文件下载
<pre>
String url = "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware.rootexplorer_140220.apk";
HttpRequest.download(url, new File("/sdcard/rootexplorer_140220.apk"), new FileDownloadCallback() {
    @Override public void onStart() {
        super.onStart();
    }

    @Override 
    public void onProgress(int progress, long networkSpeed) {
        super.onProgress(progress, networkSpeed);
        mPbDownload.setProgress(progress);
        //String speed = FileUtils.generateFileSize(networkSpeed);
    }

    @Override 
    public void onFailure() {
        super.onFailure();
        Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_SHORT).show();
    }

    @Override 
    public void onDone() {
        super.onDone();
        Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
    }
</pre>