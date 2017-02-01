package com.tokopedia.seller.topads.view.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.other.RadioButtonItem;
import com.tokopedia.seller.topads.view.adapter.TopAdsBasicRadioButtonAdapter;
import com.tokopedia.seller.topads.view.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public abstract class TopAdsFilterRadioButtonFragment extends TopAdsFilterContentFragment implements TopAdsBasicRadioButtonAdapter.Callback {

    private TopAdsBasicRadioButtonAdapter adapter;
    private RecyclerView recyclerView;
    protected int selectedPosition;
    protected RadioButtonItem selectedRadioButtonItem;

    protected abstract List<RadioButtonItem> getRadioButtonList();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_group_name;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        adapter = new TopAdsBasicRadioButtonAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setData(getRadioButtonList());
        adapter.setSelectedPosition(selectedPosition);
        adapter.setCallback(this);
    }

    @Override
    public void onItemSelected(RadioButtonItem radioButtonItem, int position) {
        selectedRadioButtonItem = radioButtonItem;
        selectedPosition = position;
    }
}