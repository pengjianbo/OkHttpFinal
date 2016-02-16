##JsonHttpRequestCallback
<pre>
RequestParams params = new RequestParams(this);
params.addFormDataPart("page", 1);
params.addFormDataPart("limit", 12);
HttpRequest.post(Api.NEW_GAME, params, new JsonHttpRequestCallback() {
    @Override
    protected void onSuccess(JSONObject jsonObject) {
        super.onSuccess(jsonObject);
        mTvResult.setText(JsonFormatUtils.formatJson(jsonObject.toJSONString()));
    }
});
</pre>