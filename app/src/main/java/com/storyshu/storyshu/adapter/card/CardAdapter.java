package com.storyshu.storyshu.adapter.card;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.base.IBaseAdapter;
import com.storyshu.storyshu.info.CardInfo;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.List;

/**
 * 故事卡片适配器
 * Created by bear on 2016/12/4.
 */

public class CardAdapter extends IBaseAdapter {
    private static final String TAG = "CardAdapter";
    private OnCardClickedListener onCardClickListener;

    public CardAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    public void setOnCardClickListener(OnCardClickedListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.card_item_layout, null);

            viewHolder = new ViewHolder();

            viewHolder.avatar = (AvatarImageView) convertView.findViewById(R.id.avatar);
            viewHolder.nickName = (TextView) convertView.findViewById(R.id.nickname);
            viewHolder.likeButton = convertView.findViewById(R.id.like_layout);
            viewHolder.opposeButton = convertView.findViewById(R.id.oppose_layout);
            viewHolder.like = (CheckBox) convertView.findViewById(R.id.like);
            viewHolder.oppose = (CheckBox) convertView.findViewById(R.id.oppose);
            viewHolder.extract = (TextView) convertView.findViewById(R.id.content);
            viewHolder.destroyTime = (TextView) convertView.findViewById(R.id.destroy_time);
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.cover_pic);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        CardInfo cardInfo = (CardInfo) getItem(position);
        if (cardInfo == null)
            return convertView;

        //是否匿名
        if (cardInfo.getAnonymous()) {
            viewHolder.avatar.setImageResource(R.drawable.avatar_wolverine);
            viewHolder.nickName.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(cardInfo.getUserInfo().getAvatar()))
                viewHolder.avatar.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.avatar_wolverine));
            else {
                Glide.with(getContext()).load(cardInfo.getUserInfo().getAvatar()).dontAnimate().into(viewHolder.avatar);
            }
            //
            viewHolder.nickName.setText(cardInfo.getUserInfo().getNickname());
        }

        viewHolder.destroyTime.setText(TimeUtils.convertDestroyTime(getContext(), cardInfo.getDestroyTime()));

        viewHolder.like.setChecked(cardInfo.getLike());
        viewHolder.oppose.setChecked(cardInfo.getOppose());

        viewHolder.extract.setText(cardInfo.getContent());
        if (!TextUtils.isEmpty(cardInfo.getCover())) {
            viewHolder.cover.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(cardInfo.getCover()).dontAnimate().into(viewHolder.cover);
        }

        /**
         * 点击
         */
        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCardClickListener != null)
                    onCardClickListener.onLikeClick(position);
            }
        });
        viewHolder.opposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCardClickListener != null)
                    onCardClickListener.onOpposeClick(position);
            }
        });

        return convertView;
    }

    private ViewHolder viewHolder;

    private class ViewHolder {
        AvatarImageView avatar; //用户头像
        TextView nickName; //用户昵称
        TextView destroyTime; //剩余时间

        CheckBox like; //点赞
        CheckBox oppose;  //喝倒彩

        View likeButton; //点赞按钮
        View opposeButton; //喝倒彩按钮


        TextView extract; //故事摘要
        ImageView cover; //故事的详情图
    }

    public interface OnCardClickedListener {
        void onLikeClick(int position);

        void onOpposeClick(int position);
    }
}
