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

public class TopAdsGroupAdListFragment extends TopAdsAdListFragment<TopAdsGroupAdListPresenter> {

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsGroupAdListPresenterImpl(context, this);
    }

    @Override
    protected void searchAd() {
        presenter.searchAd(startDate, endDate, keyword, status, page);
    }

    public static Fragment createInstance() {
        TopAdsGroupAdListFragment fragment = new TopAdsGroupAdListFragment();
        return fragment;
    }
}
