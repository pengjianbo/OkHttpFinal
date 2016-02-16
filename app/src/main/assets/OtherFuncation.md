##配置OkHttpFinal
<pre>
List<Part> commomParams = new ArrayList<>();
Headers commonHeaders = new Headers.Builder().build();

OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
		.setCommenParams(commomParams)
		.setCommenHeaders(commonHeaders)
		.setTimeout(Constants.REQ_TIMEOUT)
				//.setCookieJar(CookieJar.NO_COOKIES)
				//.setCertificates(...)
				//.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
		.setDebug(true);
OkHttpFinal.getInstance().init(builder.build());
</pre>