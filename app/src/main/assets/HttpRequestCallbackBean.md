##实例代码
<pre>
RequestParams params = new RequestParams(this);
params.put("page", page);
params.put("limit", 12);
HttpRequest.post(Api.NEW_GAME, params, new MyBaseHttpRequestCallback<NewGameResponse>() {
    @Override
    public void onLogicSuccess(NewGameResponse newGameResponse) {
        super.onSuccess(newGameResponse);
        mPage = page + 1;
        if (newGameResponse.getData() != null) {
            mGameList.addAll(newGameResponse.getData());
            mNewGameListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getBaseContext(), newGameResponse.getMsg(), Toast.LENGTH_SHORT).show();
        }

        if (newGameResponse.getData() != null && newGameResponse.getData().size() > 0) {
            mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.BOTH);
        } else {
            mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.TOP);
        }
    }

    @Override
    public void onLogicFailure(NewGameResponse newGameResponse) {
        super.onLogicFailure(newGameResponse);
        String msg = newGameResponse.getMsg();
        if (StringUtils.isEmpty(msg)) {
            msg = "网络异常";
        }
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(int errorCode, String msg) {
        super.onFailure(errorCode, msg);
        Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
    }

    @Override public void onFinish() {
        super.onFinish();
        mSwipeLayout.setRefreshing(false);
    }
});
</pre>