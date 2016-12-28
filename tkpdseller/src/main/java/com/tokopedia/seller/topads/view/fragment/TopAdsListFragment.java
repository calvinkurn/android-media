package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.presenter.TopAdsAdListPresenter;
import com.tokopedia.seller.topads.view.adapter.TopAdsAdListAdapter;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsListFragment<T extends TopAdsAdListPresenter> extends BasePresenterFragment<T> implements TopAdsListPromoViewListener {

    @BindView(R2.id.list_product)
    RecyclerView listProduct;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.mainView)
    View mainView;

    protected Date startDate;
    protected Date endDate;

    protected TopAdsAdListAdapter adapter;
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
        return R.layout.fragment_top_ads_list;
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
                presenter.loadMore(startDate, endDate, lastItemPosition, visibleItem);
            }
        });
    }

    @Override
    protected void initialVar() {
        initialDate();
        refresh = new RefreshHandler(getActivity(), mainView, onRefreshListener());
        adapter = new TopAdsAdListAdapter();
    }

    private void initialDate() {
        try {
            startDate = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).parse(getActivity().getIntent().getStringExtra(TopAdsNetworkConstant.PARAM_START_DATE));
            endDate = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).parse(getActivity().getIntent().getStringExtra(TopAdsNetworkConstant.PARAM_END_DATE));
        } catch (Exception e) {

        }
    }

    @Override
    protected void setActionVar() {
        presenter.getListTopAdsFromNet(startDate, endDate);
    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {

            }
        };
    }

    @MenuRes
    public abstract int getMenuActionSelected();

    public abstract boolean getActionOnSelectedMenu(ActionMode actionMode, MenuItem menuItem);

    @Override
    public void onSearchAdLoaded(@NonNull List adList) {
        adapter.addData(adList);
    }

    @Override
    public void onLoadSearchAdError() {

    }

    @Override
    public void onTurnOnAdSuccess() {

    }

    @Override
    public void onTurnOnAdFailed() {

    }

    @Override
    public void onTurnOffAdSuccess() {

    }

    @Override
    public void onTurnOffAdFailed() {

    }
}
