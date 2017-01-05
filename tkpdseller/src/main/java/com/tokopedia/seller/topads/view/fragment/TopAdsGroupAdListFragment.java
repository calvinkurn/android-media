package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.presenter.TopAdsGroupAdListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsGroupAdListPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupAdListActivity;

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

    @Override
    public void moveToDetail(Ad ad) {
        if(ad instanceof GroupAd){
            Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_DETAIL_DATA, (GroupAd) ad);
            startActivity(intent);
        }
    }
}