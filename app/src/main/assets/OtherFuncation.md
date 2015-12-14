##配置OkHttpFinal
<pre>
Map<String, String> commonParamMap = new HashMap<>();
Map<String, String> commonHeaderMap = new HashMap<>();

OkHttpFinal okHttpFinal = new OkHttpFinal.Builder()
	.setCommenParams(commonParamMap)
	.setCommenHeader(commonHeaderMap)
	.setTimeout(Constants.REQ_TIMEOUT)
	.setDebug(true)
	//.setCertificates(...)
	//.setHostnameVerifier(new SkirtHttpsHostnameVerifier()
	.build();
okHttpFinal.init();
</pre>