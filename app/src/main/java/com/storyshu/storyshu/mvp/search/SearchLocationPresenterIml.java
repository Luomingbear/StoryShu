package com.storyshu.storyshu.mvp.search;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.adapter.poi.SearchLocationAdapter;
import com.storyshu.storyshu.info.LocationInfo;
import com.storyshu.storyshu.model.location.ILocationQueryTool;
import com.storyshu.storyshu.mvp.base.IBasePresenter;
import com.storyshu.storyshu.utils.ListUtil;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp
 * 搜索地点的页面控制实现
 * Created by bear on 2017/4/1.
 */

public class SearchLocationPresenterIml extends IBasePresenter<SearchLocationView> implements SearchLocationPresenter {
    private static final String TAG = "SearchLocationPresenter";
    private ILocationQueryTool locationQueryTool; //w位置搜索工具
    private List<LocationInfo> mLocationList; //搜索的结果的展示
    private SearchLocationAdapter mSearchLocationAdapter; //列表的位置适配器

    public SearchLocationPresenterIml(Context mContext, SearchLocationView mvpView) {
        super(mContext, mvpView);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mLocationList = new ArrayList<>();

        mSearchLocationAdapter = new SearchLocationAdapter(mContext, mLocationList);
        mSearchLocationAdapter.setOnSearchItemClickListener(onSearchItemClickListener);
        mMvpView.getSearchItemRv().setLayoutManager(new LinearLayoutManager(mContext));
        mMvpView.getSearchItemRv().setAdapter(mSearchLocationAdapter);

        locationQueryTool = new ILocationQueryTool(mContext);

    }

    private SearchLocationAdapter.OnSearchItemClickListener onSearchItemClickListener = new SearchLocationAdapter.OnSearchItemClickListener() {
        @Override
        public void onGoClick(LocationInfo locationInfo) {
            mMvpView.showToast(locationInfo.getTitle());
        }
    };

    @Override
    public void initSearchEdit() {
//        mMvpView.getSearchEt().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        mMvpView.getSearchEt().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //是否点击的回车
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        search();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMvpView.getSearchEt().requestFocus();
                            }
                        }, 300);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 搜索的结果的处理
     */
    private PoiSearch.OnPoiSearchListener onPoiSearchListener = new PoiSearch.OnPoiSearchListener() {
        @Override
        public void onPoiSearched(PoiResult poiResult, int i) {
            mLocationList.clear();
            //获取poi成功
            if (poiResult.getPois().size() > 0) {
                for (PoiItem poiItem : poiResult.getPois()) {
                    LocationInfo location = new LocationInfo();
                    location.setLocationId(poiItem.getPoiId());
                    location.setTitle(poiItem.getTitle());
                    location.setDescribe(poiItem.getProvinceName() + poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());
                    location.setLatLng(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));
                    mLocationList.add(location);
                }

                ListUtil.removeDuplicate(mLocationList);

            } else {
                mMvpView.showToast(R.string.search_error_empty);
            }

            //更新数据
            mSearchLocationAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {
            Log.i(TAG, "onPoiItemSearched: poiItem：" + poiItem.getTitle());
        }
    };

    /**
     * 搜索位置
     */
    @Override
    public void search() {
        if (locationQueryTool != null) {
            locationQueryTool.startPoiSearch(mMvpView.getSearchEt().getText().toString(),
                    ISharePreference.getCityName(mContext), onPoiSearchListener);
        }
    }
}
