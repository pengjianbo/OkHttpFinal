##StringHttpRequestCallback
<pre>
RequestParams params = new RequestParams(this);
params.addFormDataPart("page", 1);
params.addFormDataPart("limit", 12);
HttpRequest.post(Api.NEW_GAME, params, new StringHttpRequestCallback() {
    @Override
    protected void onSuccess(String s) {
        super.onSuccess(s);
        mTvResult.setText(JsonFormatUtils.formatJson(s));
    }
});
</pre>