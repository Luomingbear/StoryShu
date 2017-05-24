package com.storyshu.storyshu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.AirPortPushInfo;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.List;

/**
 * 生成候机厅的推送卡片
 * Created by bear on 2017/3/19.
 */

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.ViewHold> {
    private static final String TAG = "AirportAdapter";
    private Context mContext;
    private List<AirPortPushInfo> mPushList;
    private OnAirportCardClickListener mAirportCardClickListener;

    public interface OnAirportCardClickListener {
        void onClick(int position);
    }

    public void setAirportCardClickListener(OnAirportCardClickListener mAirportCardClickListener) {
        this.mAirportCardClickListener = mAirportCardClickListener;
    }

    public AirportAdapter(Context context, List<AirPortPushInfo> pushList) {
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
                Glide.with(mContext).load(pushInfo.getCover()).into(holder.cover);
                holder.destroyTime.setText(TimeUtils.convertDestroyTime(mContext, pushInfo.getDestroyTime()));
                break;

            case AirPortPushInfo.TYPE_STORY:

                if (pushInfo.getAnonymous()) {
                    holder.avatar.setImageResource(R.drawable.avatar_wolverine);
                    holder.nickName.setVisibility(View.GONE);
                } else {
                    Glide.with(mContext).load(pushInfo.getUserInfo().getAvatar()).into(holder.avatar);
                    holder.nickName.setVisibility(View.VISIBLE);
                    holder.nickName.setText(pushInfo.getUserInfo().getNickname());
                }

                holder.like.setNum(pushInfo.getLikeNum());
                holder.oppose.setNum(pushInfo.getOpposeNum());
                holder.comment.setNum(pushInfo.getCommentNum());
                holder.content.setText(pushInfo.getContent());
                holder.location.setText(pushInfo.getLocationTitle());

                if (!TextUtils.isEmpty(pushInfo.getCover())) {
                    holder.cover.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(pushInfo.getCover()).into(holder.cover);
                } else {
                    holder.cover.setVisibility(View.GONE);
                }

                holder.destroyTime.setText(TimeUtils.convertDestroyTime(mContext, pushInfo.getDestroyTime()));
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mPushList == null ? 0 : mPushList.size();
    }

    protected class ViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        AvatarImageView avatar; //用户头像
        TextView nickName; //用户昵称
        TextView destroyTime; //剩余时间

        ClickButton like; //点赞
        ClickButton oppose;  //喝倒彩
        ClickButton comment;  //喝倒彩

        TextView content; //故事摘要
        ImageView cover; //故事的详情图/广告详情图

        TextView location; //地点

        ViewHold(View itemView, int pushType) {
            super(itemView);
            //整体点击⌚️
            itemView.setOnClickListener(this);

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
                    comment = (ClickButton) itemView.findViewById(R.id.comment);
                    content = (TextView) itemView.findViewById(R.id.content);
                    destroyTime = (TextView) itemView.findViewById(R.id.destroy_time);
                    cover = (ImageView) itemView.findViewById(R.id.cover_pic);
                    location = (TextView) itemView.findViewById(R.id.location);
                    break;
            }

        }

        @Override
        public void onClick(View v) {
            if (mAirportCardClickListener != null)
                mAirportCardClickListener.onClick(getLayoutPosition());
        }
    }
}
