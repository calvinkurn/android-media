package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenterImpl;
import com.tokopedia.seller.topads.presenter.TopAdsSingleListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsSingleListPresenterImpl;
import com.tokopedia.seller.topads.view.adapter.TopAdsSingleListAdapter;

/**
 * Created by zulfikarrahman on 12/16/16.
 */

public class TopAdsSingleListFragment extends TopAdsListFragment<TopAdsSingleListAdapter, TopAdsSingleListPresenter> {

    @Override
    public TopAdsSingleListAdapter getAdapter() {
        return new TopAdsSingleListAdapter(getActivity(), presenter.getListTopAds());
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
        presenter = new TopAdsSingleListPresenterImpl(context, this);
    }

    public static Fragment createInstance() {
        TopAdsSingleListFragment fragment = new TopAdsSingleListFragment();
        return fragment;
    }
}
