package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.storyshu.storyshu.tool.observable.EventObserver;

/**
 * 增加观察者通知的文本框
 * Created by ming.luo on 10/28/2016.
 */

public class NotifyTextView extends AppCompatTextView implements EventObserver {
    private int questionId = -1;

    public NotifyTextView(Context context) {
        super(context);
    }

    public NotifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public void onNotify(Object sender, int eventId, Object... args) {
        if (eventId == getQuestionId()) {
            this.setText(args[0].toString());
        }
    }
}
