package com.tokopedia.seller.topads.view.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.seller.R;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingShipping;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenterImpl;
import com.tokopedia.seller.topads.presenter.TopAdsListView;
import com.tokopedia.seller.topads.view.adapter.TopAdsListAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class TopAdsItemsActivity extends BasePresenterActivity<TopAdsListPresenter> implements TopAdsListView {

    @Bind(R2.id.list_product)
    RecyclerView listProduct;

    @Bind(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @Bind(R2.id.mainView)
    View mainView;

    TopAdsListAdapter adapter;
    RefreshHandler refresh;


    ModalMultiSelectorCallback selectionMode = new ModalMultiSelectorCallback(multiSelector) {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, menu);
            FragmentSellingShipping.this.actionMode = actionMode;
            disableFilter();
            actionMode.setTitle("1");
            getActivity().getMenuInflater().inflate(com.tokopedia.core.R.menu.shipping_confirm_multi, menu);
            refresh.setPullEnabled(false);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == com.tokopedia.core.R.id.action_multi_send) {
                // Need to finish the action mode before doing the following,
                // not after. No idea why, but it crashes.
                presenter.onMultiConfirm(actionMode, multiSelector.getSelectedPositions());
                adapter.notifyDataSetChanged();
                return true;

            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            super.onDestroyActionMode(actionMode);
            enableFilter();
            refresh.setPullEnabled(true);
            multiSelector.clearSelections();
            presenter.updateListDataChecked(false);
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
        return "TopAdsItemsActivity";
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
        adapter = new TopAdsListAdapter(this, presenter.getListTopAds());
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
