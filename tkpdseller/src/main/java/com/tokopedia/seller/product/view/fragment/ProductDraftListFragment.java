package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;

import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListFragment extends TopAdsBaseListFragment {
    public static final String TAG = ProductDraftListFragment.class.getSimpleName();

    public static ProductDraftListFragment newInstance() {
        Bundle args = new Bundle();
        
        ProductDraftListFragment fragment = new ProductDraftListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClicked(Object o) {

    }

    @Override
    protected TopAdsBaseListAdapter getNewAdapter() {
        return null;
    }
}
