package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.presenter.TopAdsProductAdListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsProductAdListPresenterImpl;

/**
 * Created by zulfikarrahman on 12/16/16.
 */

public class TopAdsProductAdListFragment extends TopAdsAdListFragment<TopAdsProductAdListPresenter> {

    private int group;

    @Override
    public int getMenuActionSelected() {
        return R.menu.promo_topads_action;
    }

    @Override
    public boolean getActionOnSelectedMenu(ActionMode actionMode, MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_edit) {
            return true;
        } else if (itemId == R.id.action_off) {
            presenter.turnOffAdList(adapter.getSelectedAds());
            return true;
        } else if (itemId == R.id.action_on) {
            presenter.turnOnAddList(adapter.getSelectedAds());
            return true;
        }
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsProductAdListPresenterImpl(context, this);
    }

    @Override
    protected void searchAd() {
        presenter.searchAd(startDate, endDate, keyword, status, group, page);
    }

    public static Fragment createInstance() {
        TopAdsProductAdListFragment fragment = new TopAdsProductAdListFragment();
        return fragment;
    }
}