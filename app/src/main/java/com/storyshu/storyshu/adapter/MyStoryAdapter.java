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
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.ClickButton;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.List;

/**
 * 我的故事卡片的适配器
 * Created by bear on 2017/5/23.
 */

public class MyStoryAdapter extends RecyclerView.Adapter<MyStoryAdapter.ViewHold> {
    private Context mContext;

    private List<CardInfo> myStoryList;

    private OnStoryCardClickListener onCardClickedListener;

    public void setOnCardClickedListener(OnStoryCardClickListener onCardClickedListener) {
        this.onCardClickedListener = onCardClickedListener;
    }

    public interface OnStoryCardClickListener {
        void onClick(int position);
    }

    public MyStoryAdapter(Context context, List<CardInfo> cardInfoList) {
        this.myStoryList = cardInfoList;
        this.mContext = context;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_story_item_layout, parent, false);
        ViewHold viewHolder = new ViewHold(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        CardInfo cardInfo = myStoryList.get(position);

        if (position > 0) {
            if (TimeUtils.getDate(cardInfo.getCreateTime()).equals(
                    TimeUtils.getDate(myStoryList.get(position - 1).getCreateTime()))) {

                holder.day.setVisibility(View.GONE);
                holder.month.setVisibility(View.GONE);
            } else {

                holder.day.setVisibility(View.VISIBLE);
                holder.month.setVisibility(View.VISIBLE);

                holder.day.setText(TimeUtils.getDay(cardInfo.getCreateTime()));
                holder.month.setText(mContext.getString(R.string.unit_month, TimeUtils.getMonth(cardInfo.getCreateTime())));
            }

        } else {

            holder.day.setVisibility(View.VISIBLE);
            holder.month.setVisibility(View.VISIBLE);

            holder.day.setText(TimeUtils.getDay(cardInfo.getCreateTime()));
            holder.month.setText(mContext.getString(R.string.unit_month, TimeUtils.getMonth(cardInfo.getCreateTime())));
        }

        Glide.with(mContext).load(cardInfo.getUserInfo().getAvatar()).into(holder.avatar);
        holder.nickName.setText(cardInfo.getUserInfo().getNickname());

        holder.content.setText(cardInfo.getContent());
        holder.location.setText(cardInfo.getLocationTitle());
        holder.destoryTime.setText(TimeUtils.convertDestroyTime(mContext, cardInfo.getDestroyTime()));

        holder.like.setNum(cardInfo.getLikeNum());
        holder.oppose.setNum(cardInfo.getOpposeNum());
        holder.comment.setNum(cardInfo.getCommentNum());

        if (TextUtils.isEmpty(cardInfo.getCover())) {
            holder.cover.setVisibility(View.GONE);
        } else {
            holder.cover.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(cardInfo.getCover()).into(holder.cover);
        }
    }

    @Override
    public int getItemCount() {
        return myStoryList == null ? 0 : myStoryList.size();
    }

    class ViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView day;
        private TextView month;

        private AvatarImageView avatar; //用户头像
        private TextView nickName; //用户昵称
        private TextView content;
        private TextView location;
        private ImageView cover;
        private TextView destoryTime;

        private ClickButton like; //点赞
        private ClickButton oppose;  //喝倒彩
        private ClickButton comment;  //喝倒彩

        public ViewHold(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            day = (TextView) itemView.findViewById(R.id.day);
            month = (TextView) itemView.findViewById(R.id.month);
            avatar = (AvatarImageView) itemView.findViewById(R.id.avatar);
            nickName = (TextView) itemView.findViewById(R.id.nickname);
            like = (ClickButton) itemView.findViewById(R.id.like);
            oppose = (ClickButton) itemView.findViewById(R.id.oppose);
            comment = (ClickButton) itemView.findViewById(R.id.comment);
            destoryTime = (TextView) itemView.findViewById(R.id.destroy_time);
            content = (TextView) itemView.findViewById(R.id.content);
            cover = (ImageView) itemView.findViewById(R.id.cover_pic);
            location = (TextView) itemView.findViewById(R.id.location);
        }

        @Override
        public void onClick(View v) {
            if (onCardClickedListener != null)
                onCardClickedListener.onClick(getLayoutPosition());
        }
    }
}
