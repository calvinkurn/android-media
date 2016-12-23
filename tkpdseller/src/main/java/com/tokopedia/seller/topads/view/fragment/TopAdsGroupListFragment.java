package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.presenter.TopAdsGroupListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsGroupListPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsGroupListAdapter;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupListFragment extends TopAdsListFragment<TopAdsGroupListAdapter, TopAdsGroupListPresenter> {
    @Override
    public TopAdsGroupListAdapter getAdapter() {
        return new TopAdsGroupListAdapter(context, presenter.getListTopAds(), this);
    }

    @Override
    public int getMenuActionSelected() {
        return R.menu.promo_topads_action;
    }

    @Override
    public boolean getActionOnSelectedMenu(ActionMode actionMode, MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_delete) {
            presenter.actionDeleteAds(adapter.getSelectedAds());
            return true;
        }else if(itemId == R.id.action_edit){
            return true;
        }else if (itemId == R.id.action_off){
            presenter.actionOffAds(adapter.getSelectedAds());
            return true;
        }else if(itemId == R.id.action_on){
            presenter.actionOnAds(adapter.getSelectedAds());
            return true;
        }
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsGroupListPresenterImpl(context, this);
    }

    public static Fragment createInstance() {
        TopAdsGroupListFragment fragment = new TopAdsGroupListFragment();
        return fragment;
    }
}
