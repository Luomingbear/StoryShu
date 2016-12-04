package com.bear.passby.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bear.passby.R;
import com.bear.passby.info.CardInfo;
import com.bear.passby.widget.imageview.RoundImageView;

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
            convertView = getInflater().inflate(R.layout.card_view_layout, null);

            viewHolder = new ViewHolder();

            viewHolder.storyPic = (RoundImageView) convertView.findViewById(R.id.card_view_story_pic);
            convertView.setTag(viewHolder);
        } else convertView.getTag();

        CardInfo cardInfo = (CardInfo) getItem(position);
        // TODO: 2016/12/4 添加数据 

        return convertView;
    }

    private ViewHolder viewHolder;

    private class ViewHolder {
        RoundImageView storyPic;
    }
}
