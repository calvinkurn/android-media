package com.tokopedia.seller.topads.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.model.RadioButtonItem;
import com.tokopedia.seller.topads.view.adapter.TopAdsBasicRadioButtonAdapter;
import com.tokopedia.seller.topads.view.widget.DividerItemDecoration;

import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */

public abstract class TopAdsFilterRadioButtonFragment<P> extends TopAdsFilterContentFragment<P> implements TopAdsBasicRadioButtonAdapter.Callback {

    private RecyclerView recyclerView;
    protected TopAdsBasicRadioButtonAdapter adapter;
    protected int selectedAdapterPosition;

    protected abstract List<RadioButtonItem> getRadioButtonList();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_group_name;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TopAdsBasicRadioButtonAdapter();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(adapter);
        setAdapterData(getRadioButtonList());
    }

    public void setAdapterData(List<RadioButtonItem> radioButtonItems){
        adapter.setData(radioButtonItems);
        adapter.notifyDataSetChanged();
        if (selectedAdapterPosition > -1) { // data might come from api
            adapter.setSelectedPosition(selectedAdapterPosition);
        }
        adapter.setCallback(this);
    }

    public String getSelectedRadioValue(){
        if ( adapter.isEmpty()) {
            return String.valueOf(0);
        }
        return adapter.getSelectedItem().getValue();
    }

    @Override
    public void onItemSelected(RadioButtonItem radioButtonItem, int position) {
        selectedAdapterPosition = position;
        if (callback != null) {
            callback.onStatusChanged(true);
        }
    }

    @Override
    public void onSaveState(Bundle bundle) {
        bundle.putInt(TopAdsExtraConstant.EXTRA_ITEM_SELECTED_POSITION, selectedAdapterPosition);
    }

    @Override
    public void onRestoreState(Bundle bundle) {
        selectedAdapterPosition = bundle.getInt(TopAdsExtraConstant.EXTRA_ITEM_SELECTED_POSITION);
    }
}