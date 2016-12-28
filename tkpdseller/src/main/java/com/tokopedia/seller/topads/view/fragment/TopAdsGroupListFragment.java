package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.presenter.TopAdsGroupAdListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsGroupAdListPresenterImpl;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupListFragment extends TopAdsListFragment<TopAdsGroupAdListPresenter> {

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
        presenter = new TopAdsGroupAdListPresenterImpl(context, this);
    }

    public static Fragment createInstance() {
        TopAdsGroupListFragment fragment = new TopAdsGroupListFragment();
        return fragment;
    }
}
