package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;

import java.util.List;

/**
 * @author normansyahputa on 5/17/17.
 */

public abstract class VariantBaseListFragment<P, T extends ItemType> extends BasePresenterFragment<P> implements
        BaseListViewListener<T>, BaseListAdapter.Callback<T> {

    //protected BaseListAdapter<T> adapter;
    // protected RecyclerView recyclerView;
    //protected LinearLayoutManager layoutManager;
    protected int totalItem;
    protected int currentPage;

    public VariantBaseListFragment() {
        // Required empty public constructor
    }

    protected abstract void searchForPage(int page);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_base_list_seller;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        // recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
//        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
//        adapter.setCallback(this);
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadData();
    }

    protected void loadData() {
//        adapter.clearData();
//        adapter.showRetryFull(false);
//        adapter.showLoadingFull(true);
//        searchForPage(currentPage);
    }

}