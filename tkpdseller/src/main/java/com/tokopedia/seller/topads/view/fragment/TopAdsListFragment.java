package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenter;
import com.tokopedia.seller.topads.view.adapter.TopAdsListAdapter;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsListFragment<T extends TopAdsListAdapter, V extends TopAdsListPresenter> extends BasePresenterFragment<V> implements TopAdsListPromoViewListener {

    @BindView(R2.id.list_product)
    RecyclerView listProduct;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.mainView)
    View mainView;

    protected T adapter;
    private RefreshHandler refresh;
    private ActionMode actionMode;
    private LinearLayoutManager layoutManager;

    public TopAdsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.promo_topads, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_top_ads_list;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listProduct.setLayoutManager(layoutManager);
        listProduct.setAdapter(adapter);
        listProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                presenter.loadMore(lastItemPosition, visibleItem);
            }
        });
    }

    @Override
    protected void initialVar() {
        refresh = new RefreshHandler(getActivity(), mainView, onRefreshListener());
        adapter = getAdapter();
    }

    @Override
    protected void setActionVar() {
        presenter.getListTopAdsFromNet();
    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {

            }
        };
    }

    @Override
    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    @Override
    public void setMenuInflater(Menu menu) {
        getActivity().getMenuInflater().inflate(getMenuActionSelected(), menu);
    }

    @Override
    public void disableRefreshPull() {
        refresh.setPullEnabled(false);
    }

    @Override
    public void enableRefreshPull() {
        refresh.setPullEnabled(true);
    }

    @Override
    public void startSupportActionMode(ModalMultiSelectorCallback selectionMode) {
        if(getActivity() instanceof  AppCompatActivity) {
            ((AppCompatActivity) getActivity()).startSupportActionMode(selectionMode);
        }else{
            throw new RuntimeException("activity not support");
        }
    }

    @Override
    public void setTitleMode(String title) {
        actionMode.setTitle(title);
    }

    @Override
    public void finishActionMode() {
        actionMode.finish();
    }

    @Override
    public void moveToDetail(int position) {

    }

    public abstract T getAdapter();

    @MenuRes
    public abstract int getMenuActionSelected();

    public abstract boolean getActionOnSelectedMenu(ActionMode actionMode, MenuItem menuItem);
}
