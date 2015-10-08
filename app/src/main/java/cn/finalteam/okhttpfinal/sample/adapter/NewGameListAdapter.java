package cn.finalteam.okhttpfinal.sample.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.sample.Global;
import cn.finalteam.okhttpfinal.sample.R;
import cn.finalteam.okhttpfinal.sample.http.MyHttpCycleContext;
import cn.finalteam.okhttpfinal.sample.http.model.GameInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/26 下午8:30
 */
public class NewGameListAdapter extends CommonBaseAdapter<NewGameListAdapter.NewGameViewHolder, GameInfo> {

    public NewGameListAdapter(MyHttpCycleContext httpCycleContext, List<GameInfo> list) {
        super(httpCycleContext, list);
    }

    @Override
    public NewGameViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.adapter_new_game_list_item, null);
        NewGameViewHolder viewHolder = new NewGameViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewGameViewHolder holder, int position) {
        GameInfo data = mList.get(position);
        holder.mTvGameName.setText(data.getName());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().displayImage(data.getIconUrl(), holder.mIvGameIcon, options);
        holder.mTvPlayingManNumber.setText(String.valueOf(data.getCommentCount()));

        if(data.getOpenState()==0 || data.getOpenState()==21){
            holder.mTvGameSocre.setVisibility(View.INVISIBLE);
        }else{
            holder.mTvGameSocre.setText(data.getTotalSocreV() + "分");
            holder.mTvGameSocre.setVisibility(View.VISIBLE);
        }

    }

    static class NewGameViewHolder extends CommonBaseAdapter.ViewHolder {
        @Bind(R.id.tv_playing_man_number)
        TextView mTvPlayingManNumber;
        @Bind(R.id.iv_game_icon)
        ImageView mIvGameIcon;
        @Bind(R.id.tv_game_name)
        TextView mTvGameName;
        @Bind(R.id.tv_game_socre)
        TextView mTvGameSocre;

        public NewGameViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            int height = (Global.SCREEN_WIDTH / 3) - 32;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            mIvGameIcon.setLayoutParams(params);
        }
    }
}
