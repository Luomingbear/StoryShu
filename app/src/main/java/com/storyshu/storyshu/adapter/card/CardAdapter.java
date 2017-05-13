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
import com.storyshu.storyshu.bean.StoryBean;
import com.storyshu.storyshu.utils.time.TimeUtils;
import com.storyshu.storyshu.widget.imageview.AvatarImageView;

import java.util.List;

/**
 * 故事卡片适配器
 * Created by bear on 2016/12/4.
 */

public class CardAdapter extends IBaseAdapter {
    private static final String TAG = "CardAdapter";

    public CardAdapter(Context context, List<?> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.card_item_layout, null);

            viewHolder = new ViewHolder();

            viewHolder.avatar = (AvatarImageView) convertView.findViewById(R.id.avatar);
            viewHolder.nickName = (TextView) convertView.findViewById(R.id.nickname);
            viewHolder.like = (CheckBox) convertView.findViewById(R.id.like);
            viewHolder.oppose = (CheckBox) convertView.findViewById(R.id.oppose);
            viewHolder.extract = (TextView) convertView.findViewById(R.id.content);
            viewHolder.destroyTime = (TextView) convertView.findViewById(R.id.destroy_time);
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.cover_pic);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        StoryBean cardInfo = (StoryBean) getItem(position);
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

        viewHolder.destroyTime.setText(TimeUtils.leftTime(getContext(), cardInfo.getDestroyTime()));

        viewHolder.extract.setText(cardInfo.getContent());
        if (!TextUtils.isEmpty(cardInfo.getCover())) {
            viewHolder.cover.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(cardInfo.getCover()).dontAnimate().into(viewHolder.cover);
        }
        return convertView;
    }

    private ViewHolder viewHolder;

    private class ViewHolder {
        AvatarImageView avatar; //用户头像
        TextView nickName; //用户昵称
        TextView destroyTime; //剩余时间

        CheckBox like; //点赞
        CheckBox oppose;  //喝倒彩

        TextView extract; //故事摘要
        ImageView cover; //故事的详情图
    }
}
