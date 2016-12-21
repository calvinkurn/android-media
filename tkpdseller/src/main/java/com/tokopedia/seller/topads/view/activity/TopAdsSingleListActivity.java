package com.tokopedia.seller.topads.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenterImpl;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;
import com.tokopedia.seller.topads.view.adapter.SingleAdsListAdapter;
import com.tokopedia.seller.topads.view.adapter.TopAdsListAdapter;

import butterknife.BindView;

public class TopAdsSingleListActivity extends BasePresenterActivity<TopAdsListPresenter> implements TopAdsListPromoViewListener {

    @BindView(R2.id.list_product)
    RecyclerView listProduct;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.mainView)
    View mainView;

    private TopAdsListAdapter adapter;
    private RefreshHandler refresh;
    private MultiSelector multiSelector = new MultiSelector();
    private ActionMode actionMode;

    ModalMultiSelectorCallback selectionMode = new ModalMultiSelectorCallback(multiSelector) {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, menu);
            TopAdsSingleListActivity.this.actionMode = actionMode;
            getMenuInflater().inflate(R.menu.promo_topads_action, menu);
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
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_delete) {
                presenter.actionDeleteAds(multiSelector.getSelectedPositions());
                return true;
            }else if(itemId == R.id.action_edit){
                return true;
            }else if (itemId == R.id.action_off){
                presenter.actionOffAds(multiSelector.getSelectedPositions());
                return true;
            }else if(itemId == R.id.action_on){
                presenter.actionOnAds(multiSelector.getSelectedPositions());
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            super.onDestroyActionMode(actionMode);
            refresh.setPullEnabled(true);
            multiSelector.clearSelections();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.promo_topads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_on) {
        } else if (itemId == R.id.action_off) {
        } else if (itemId == R.id.action_delete) {
        } else if (itemId == R.id.action_edit) {
        } else {
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getScreenName() {
        return "TopAdsSingleListActivity";
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsListPresenterImpl(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_ads_list;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        listProduct.setLayoutManager(new LinearLayoutManager(this));
        listProduct.setAdapter(adapter);
    }

    @Override
    protected void initVar() {
        refresh = new RefreshHandler(this, mainView, onRefreshListener());
        adapter = new SingleAdsListAdapter(this, presenter.getListTopAds(), this);
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

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getListTopAdsFromNet();
    }
}
