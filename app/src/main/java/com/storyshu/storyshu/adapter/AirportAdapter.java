package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.utils.time.TimeConvertUtil;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.ArrayList;

/**
 * 生成候机厅的推送卡片
 * Created by bear on 2017/3/19.
 */

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.ViewHold> {
    private Context mContext;
    private ArrayList<AirPortPushInfo> mPushList;

    public AirportAdapter(Context context, ArrayList<AirPortPushInfo> pushList) {
        mContext = context;
        mPushList = pushList;
    }

    @Override
    public int getItemViewType(int position) {
        return mPushList.get(position).getPushType();
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHold viewHold;
        switch (viewType) {
            case AirPortPushInfo.TYPE_AD:
                view = LayoutInflater.from(mContext).inflate(R.layout.ad_item_layout, parent, false);
                viewHold = new ViewHold(view, viewType);
                return viewHold;
            case AirPortPushInfo.TYPE_STORY:
                view = LayoutInflater.from(mContext).inflate(R.layout.push_card_layout, parent, false);
                viewHold = new ViewHold(view, viewType);
                return viewHold;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        AirPortPushInfo pushInfo = mPushList.get(position);
        switch (pushInfo.getPushType()) {
            case AirPortPushInfo.TYPE_AD:
                ImageLoader.getInstance().displayImage(pushInfo.getDetailPic(), holder.cover);
                holder.destroyTime.setText(TimeConvertUtil.destroyTime(mContext, pushInfo.getCreateDate(), pushInfo.getLifeTime()));
                break;

            case AirPortPushInfo.TYPE_STORY:
                ImageLoader.getInstance().displayImage(pushInfo.getUserInfo().getAvatar(), holder.avatar);
                holder.nickName.setText(pushInfo.getUserInfo().getNickname());
                holder.like.setNum(pushInfo.getLikeNum());
                holder.oppose.setNum(pushInfo.getOpposeNum());
                holder.content.setText(pushInfo.getContent());
                holder.location.setText(pushInfo.getLocation());
                holder.cover.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(pushInfo.getDetailPic(), holder.cover);
                holder.destroyTime.setText(TimeConvertUtil.destroyTime(mContext, pushInfo.getCreateDate(), pushInfo.getLifeTime()));
                break;
        }

    }


    @Override
    public int getItemCount() {
        return mPushList == null ? 0 : mPushList.size();
    }

    protected class ViewHold extends RecyclerView.ViewHolder {
        AvatarImageView avatar; //用户头像
        TextView nickName; //用户昵称
        TextView destroyTime; //剩余时间

        ClickButton like; //点赞
        ClickButton oppose;  //喝倒彩

        TextView content; //故事摘要
        ImageView cover; //故事的详情图/广告详情图

        TextView location; //地点

        ViewHold(View itemView, int pushType) {
            super(itemView);

            switch (pushType) {
                case AirPortPushInfo.TYPE_AD:
                    cover = (ImageView) itemView.findViewById(R.id.cover);
                    destroyTime = (TextView) itemView.findViewById(R.id.destroy_time);
                    break;
                case AirPortPushInfo.TYPE_STORY:
                    avatar = (AvatarImageView) itemView.findViewById(R.id.avatar);
                    nickName = (TextView) itemView.findViewById(R.id.nickname);
                    like = (ClickButton) itemView.findViewById(R.id.like);
                    oppose = (ClickButton) itemView.findViewById(R.id.oppose);
                    content = (TextView) itemView.findViewById(R.id.content);
                    destroyTime = (TextView) itemView.findViewById(R.id.destroy_time);
                    cover = (ImageView) itemView.findViewById(R.id.cover_pic);
                    location = (TextView) itemView.findViewById(R.id.location);
                    break;
            }

        }
    }
}
