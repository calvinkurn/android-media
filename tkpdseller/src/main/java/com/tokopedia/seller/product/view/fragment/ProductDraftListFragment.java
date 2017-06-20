package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;

import com.tokopedia.seller.product.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordAdapter;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.model.Ad;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListFragment extends TopAdsBaseListFragment<ProductDraftListPresenter, String>{
    public static final String TAG = ProductDraftListFragment.class.getSimpleName();

    public static ProductDraftListFragment newInstance() {
        return new ProductDraftListFragment();
    }

    @Override
    protected TopAdsBaseListAdapter getNewAdapter() {
        return new TopAdsKeywordAdapter();
    }

    @Override
    public void onItemClicked(String s) {

    }
}
