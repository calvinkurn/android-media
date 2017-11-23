package com.tokopedia.seller.base.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.TopAdsFilterAdapter;
import com.tokopedia.seller.base.view.model.FilterTitleItem;

import java.util.ArrayList;

/**
 * Created by Nathaniel on 1/27/2017.
 */
public class TopAdsFilterListFragment extends BasePresenterFragment implements TopAdsFilterAdapter.Callback {

    public static final String EXTRA_TITLE_ITEM_LIST = "EXTRA_TITLE_ITEM_LIST";
    public static final String EXTRA_ITEM_SELECTED_POSITION = "EXTRA_ITEM_SELECTED_POSITION";
    
    private TopAdsFilterAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<FilterTitleItem> filterTitleItemList;
    private Callback callback;
    private int selectedItem;

    public static TopAdsFilterListFragment createInstance(ArrayList<FilterTitleItem> filterTitleItemList, int selectedPosition) {
        TopAdsFilterListFragment fragment = new TopAdsFilterListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_TITLE_ITEM_LIST, filterTitleItemList);
        bundle.putInt(EXTRA_ITEM_SELECTED_POSITION, selectedPosition);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TopAdsFilterAdapter();
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        filterTitleItemList = bundle.getParcelableArrayList(EXTRA_TITLE_ITEM_LIST);
        selectedItem = bundle.getInt(EXTRA_ITEM_SELECTED_POSITION);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_filter_list;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        recyclerView.setAdapter(adapter);
        adapter.setData(filterTitleItemList);
        adapter.setCallback(this);
        selectItem(selectedItem);
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

    @Override
    public void onItemSelected(int position) {
        if (callback != null) {
            callback.onItemSelected(position);
        }
    }

    public void setActive(int position, boolean active) {
        FilterTitleItem filterTitleItem = filterTitleItemList.get(position);
        filterTitleItem.setActive(active);
        adapter.notifyItemChanged(position);
    }

    public void selectItem(int position) {
        if (adapter != null && isAdded()) {
            adapter.selectItem(position);
        }
    }

    public int getCurrentPosition() {
        return selectedItem;
    }

    public interface Callback {

        void onItemSelected(int position);

    }
}