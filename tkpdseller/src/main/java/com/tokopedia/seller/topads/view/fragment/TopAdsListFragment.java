package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsSingleListActivity;
import com.tokopedia.seller.topads.view.adapter.TopAdsListAdapter;
import com.tokopedia.seller.topads.view.adapter.TopAdsSingleListAdapter;
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
    protected MultiSelector multiSelector = new MultiSelector();
    private ActionMode actionMode;

    public TopAdsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.promo_topads, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    ModalMultiSelectorCallback selectionMode = new ModalMultiSelectorCallback(multiSelector) {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, menu);
            TopAdsListFragment.this.actionMode = actionMode;
            getActivity().getMenuInflater().inflate(getMenuActionSelected(), menu);
            refresh.setPullEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle(String.valueOf(multiSelector.getSelectedPositions().size()));
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return getActionOnSelectedMenu(actionMode, menuItem);
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            super.onDestroyActionMode(actionMode);
            refresh.setPullEnabled(true);
            multiSelector.clearSelections();
        }
    };

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
        listProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        listProduct.setAdapter(adapter);
    }

    @Override
    protected void initialVar() {
        refresh = new RefreshHandler(getActivity(), mainView, onRefreshListener());
        adapter = getAdapter();
    }

    @Override
    protected void setActionVar() {

    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {

            }
        };
    }

    public abstract T getAdapter();

    @MenuRes
    public abstract int getMenuActionSelected();

    protected abstract boolean getActionOnSelectedMenu(ActionMode actionMode, MenuItem menuItem);
}
