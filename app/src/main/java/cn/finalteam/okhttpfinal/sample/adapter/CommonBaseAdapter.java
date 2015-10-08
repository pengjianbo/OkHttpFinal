package cn.finalteam.okhttpfinal.sample.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.sample.http.MyHttpCycleContext;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/26 下午8:05
 */
public abstract class CommonBaseAdapter<VH extends CommonBaseAdapter.ViewHolder, T> extends BaseAdapter {

    protected Context mContext;
    protected MyHttpCycleContext mHttpCycleContext;
    protected List<T> mList;
    public LayoutInflater mInflater;

    protected ProgressDialog mProgressDialog;

    public CommonBaseAdapter(MyHttpCycleContext context, List<T> list) {
        this.mHttpCycleContext = context;
        this.mContext = context.getContext();
        this.mList = list;
        this.mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public T getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        VH holder = null;
        if ( view == null ) {
            holder = onCreateViewHolder(viewGroup, i);
            holder.view.setTag(holder);
        } else {
            holder = (VH) view.getTag();
        }

        onBindViewHolder(holder, i);
        return holder.view;
    }

    public View inflate(int resLayout, ViewGroup parent) {
        return mInflater.inflate(resLayout, parent, false);
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int position);
    public abstract void onBindViewHolder(VH holder, int position);

    public static class ViewHolder {
        View view;
        public ViewHolder(View view) {
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }

}

