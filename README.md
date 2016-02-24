![](images/okhttpfinal.jpg)

# OkHttpFinal简介
一个对OkHttp封装的简单易用型HTTP请求和文件下载管理框架。更多详情请查看[WIKI](https://github.com/pengjianbo/OkHttpFinal/wiki)

* 简化[OkHttp](https://github.com/square/okhttp)使用
* 支持GET,POST,PUT,DELETE,HEAD,PATCH谓词
* 支持Activity和Fragment生命周期结束后终止请求
* 支持Download Manager功能
* 支持文件下载多事件回调
* 支持返回bean对象
* 支持返回json String数据
* 支持返回JsonObject对象
* 支持https请求
* 支持https证书访问
* 支持文件上传
* 支持全局params
* 支持全局header
* 支持http cancel
* ……

#下载OkHttpFinal
下载这个[JAR](https://github.com/pengjianbo/OkHttpFinal/tree/master/downloads) 或者通过Gradle抓取:

```gradle
compile 'cn.finalteam:okhttpfinal:2.0.4'
```

##eclipse使用
下载OkHttpFinal对应的[Jar](https://github.com/pengjianbo/OkHttpFinal/tree/master/downloads) 和下载依赖[Extra Jar](https://github.com/pengjianbo/OkHttpFinal/tree/master/downloads/extra)

## 2.0.4更新内容
* 更新tools库，解决日志循环输出bug

---
Demo apk:![DEMO APK](images/okhttpfianl-sample-qrcode.png)
# 如何使用
1、在你App Application中初始化OkHttpFinal(此初始化只是简单赋值不会阻塞线程)
```java
OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
OkHttpFinal.getInstance().init(builder.build());
```

2、请求接口
```java
List<File> files = new ArrayList<>();
File file = new File("...");
RequestParams params = new RequestParams(this);//请求参数
params.addFormDataPart("username", mUserName);//表单数据
params.addFormDataPart("password", mPassword);//表单数据
params.addFormDataPart("file", file);//上传单个文件
params.addFormDataPart("files", files);//上传多个文件
params.addHeader("token", token);//添加header信息
HttpRequest.post(Api.LOGIN, params, new BaseHttpRequestCallback<LoginResponse>() {

//请求网络前
@Override
public void onStart() {
	buildProgressDialog().show();
}

//这里只是网络请求成功了（也就是服务器返回JSON合法）没有没有分装具体的业务成功与失败，大家可以参考demo去分装自己公司业务请求成功与失败
@Override
protected void onSuccess(LoginResponse loginResponse) {
	toast(loginResponse.getMsg());
}

//请求失败（服务返回非法JSON、服务器异常、网络异常）
@Override
public void onFailure(int errorCode, String msg) {
	toast("网络异常~，请检查你的网络是否连接后再试");
}
 
//请求网络结束   
@Override
public void onFinish() {
    dismissProgressDialog();
}
```
## 下载文件
```java
String url = "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware.rootexplorer_140220.apk";
File saveFile = new File("/sdcard/rootexplorer_140220.apk");
HttpRequest.download(url, saveFile, new FileDownloadCallback() {
    //开始下载
    @Override 
    public void onStart() {
        super.onStart();
    }

	//下载进度
    @Override
    public void onProgress(int progress, long networkSpeed) {
        super.onProgress(progress, networkSpeed);
        mPbDownload.setProgress(progress);
        //String speed = FileUtils.generateFileSize(networkSpeed);
    }

	//下载失败
    @Override 
    public void onFailure() {
        super.onFailure();
        Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_SHORT).show();
    }

	//下载完成（下载成功）
    @Override 
    public void onDone() {
        super.onDone();
        Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
    }
});
```
**更多功能请查看[WIKI](https://github.com/pengjianbo/OkHttpFinal/wiki)**

# 代码混淆
```properties
#--------------- BEGIN: okhttp ----------
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
#--------------- END: okhttp ----------
#--------------- BEGIN: okio ----------
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
#--------------- END: okio ----------
```

### [更新日志](https://github.com/pengjianbo/OkHttpFinal/tree/master/CHANGELOG.md)

# 关于作者
* **QQ:**172340021   
* **QQ群:**218801658  
* **Email:**<pengjianbo@finalteam.cn>

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.