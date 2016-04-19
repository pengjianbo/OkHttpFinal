# 更新日志
# V2.0.7
* 解决请求慢的问题
* 解决请求A未反应，立即请求B，B请求不下来
* 解决post 参数为空请求报405问题
* 优化网络请求

# V2.0.6
* RequestParams添加setCacheControl方法
* HttpRequst添加多态方法，配置当前请求超时时间
* 支持自定义OkHttpClient.Builder配置

# V2.0.5
* 添加onResponse重载方法，返回okhttp请求Response实例
* 添加SSLSocketFactory和Dispatcher配置方法

# V2.0.4
* 更新tools库，解决日志循环输出bug

# V2.0.3
* 添加string等多种数据类型作为请求体
* 添加几个缓存配置方法

# V2.0.2
* 添加拦截器
* 解决onProgress回调方法在非Main线程执行问题
* 页面销毁自动cancel

# V2.0.1
* 解决获取响应header bug
* 修改applicationJson方法

## V2.0.0
* 更新OkHttp3
* 下载管理分离
* 解决公共参数更新bug
* 解决List<Bean>类型转换问题
* 添加JsonArrayHttpRequestCallback回调类
* 多文件上传
* 添加URL编码
* 请求参数支持添加文件集合
* 整理代码/优化设计

## V1.2.3
* 更新OkHttp3
* 添加获取header方法

## V1.2.2
* 去掉json合法性验证

## V1.2.1
* fastjson gradle引入

## V1.2.0
* 添加PUT,DELETE,HEAD,PATCH谓词
* 支持http cancel
* 全局配置debug模式
* 添加防止公共params和公共header为Null情况
* RequestParams 添加setJSONObject方法（对application/json支持）
* RequestParams 添加setCustomRequestBody方法
* …… 

## V1.1.0
* 上传文件进度
* 支持https
* https证书访问
* 简单文件下载功能
* 支持多种返回数据结构
* 添加配置全局params
* 添加配置全局header
* ……