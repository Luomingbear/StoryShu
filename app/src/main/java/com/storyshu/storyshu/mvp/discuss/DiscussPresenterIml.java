package com.storyshu.storyshu.mvp.discuss;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.DiscussAdapter;
import com.storyshu.storyshu.info.DiscussItemInfo;
import com.storyshu.storyshu.model.discuss.DiscussModel;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.KeyBordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp
 * 讨论的控制
 * Created by bear on 2017/5/24.
 */

public class DiscussPresenterIml extends IBasePresenter<DiscussView> implements DiscussPresenter {
    private static final String TAG = "DiscussPresenterIml";
    private List<DiscussItemInfo> mDiscussList; //讨论列表

    private DiscussAdapter mDiscussAdapter;

    public DiscussPresenterIml(Context mContext, DiscussView mvpView) {
        super(mContext, mvpView);
        mDiscussList = new ArrayList<>();

        mDiscussAdapter = new DiscussAdapter(mContext, mDiscussList);
        mMvpView.getDiscussRv().setAdapter(mDiscussAdapter);
        mMvpView.getDiscussRv().setLayoutManager(new LinearLayoutManager(mContext));

        /**
         * 设置滑动列表就隐藏键盘
         */
        mMvpView.getDiscussRv().addOnItemTouchListener(onItemTouchListener);
    }

    private RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    KeyBordUtil.hideKeyboard(mContext, mMvpView.getEditText());
                    break;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    @Override
    public void initDiscussData() {

    }

    @Override
    public void sendMessage() {
        if (mMvpView.getEditText().getText().length() == 0) {
            mMvpView.showToast(R.string.comment_issue_empty);
            return;
        }

        DiscussModel discussModel = new DiscussModel(mContext);
        discussModel.sendMessage(mMvpView.getEditText().getText().toString());
        mDiscussAdapter.notifyDataSetChanged();

        //滚动到最新的数据
        mMvpView.getDiscussRv().scrollToPosition(mDiscussList.size() - 1);

        mMvpView.getEditText().setText("");

    }
}
