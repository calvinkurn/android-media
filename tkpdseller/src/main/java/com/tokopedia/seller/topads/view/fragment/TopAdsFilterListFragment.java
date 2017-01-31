package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.other.FilterTitleItem;
import com.tokopedia.seller.topads.view.adapter.TopAdsFilterAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/27/2017.
 */

public class TopAdsFilterListFragment extends BasePresenterFragment {

    private static final String EXTRA_TITLE_ITEM_LIST = "EXTRA_TITLE_ITEM_LIST";

    public static TopAdsFilterListFragment createInstance(ArrayList<FilterTitleItem> filterTitleItemList) {
        TopAdsFilterListFragment fragment = new TopAdsFilterListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_TITLE_ITEM_LIST, filterTitleItemList);
        fragment.setArguments(bundle);
        return fragment;
    }

    private TopAdsFilterAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<FilterTitleItem> filterTitleItemList;

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {

    }

    @Override
    public void onRestoreState(Bundle bundle) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle bundle) {
        filterTitleItemList = bundle.getParcelableArrayList(EXTRA_TITLE_ITEM_LIST);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_list;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TopAdsFilterAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setData(filterTitleItemList);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }
}