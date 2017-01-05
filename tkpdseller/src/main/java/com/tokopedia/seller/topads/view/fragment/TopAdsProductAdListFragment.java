package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.presenter.TopAdsProductAdListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsProductAdListPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailProductActivity;

/**
 * Created by zulfikarrahman on 12/16/16.
 */

public class TopAdsProductAdListFragment extends TopAdsAdListFragment<TopAdsProductAdListPresenter> {

    private int group;

    public static Fragment createInstance(int group) {
        TopAdsProductAdListFragment fragment = new TopAdsProductAdListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_GROUP, group);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onFirstTimeLaunched() {
        super.onFirstTimeLaunched();
        group = getArguments().getInt(TopAdsExtraConstant.EXTRA_GROUP);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsProductAdListPresenterImpl(context, this);
    }

    @Override
    protected void searchAd() {
        presenter.searchAd(startDate, endDate, keyword, status, group, page);
    }

    @Override
    public void moveToDetail(Ad ad) {
        if(ad instanceof ProductAd){
            Intent intent = new Intent(getActivity(), TopAdsDetailProductActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_DETAIL_DATA, (ProductAd) ad);
            startActivity(intent);
        }
    }
}