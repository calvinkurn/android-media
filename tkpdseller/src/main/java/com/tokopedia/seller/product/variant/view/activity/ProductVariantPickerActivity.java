package com.tokopedia.seller.product.variant.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.topads.dashboard.view.fragment.ChipsTopAdsSelectionFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAddProductListFragment;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantPickerActivity extends BasePickerMultipleItemActivity {

    @Override
    public Fragment getSearchListFragment() {
        return TopAdsAddProductListFragment.newInstance(50);
    }

    @Override
    public Fragment getCacheListFragment() {
        return ChipsTopAdsSelectionFragment.newInstance();
    }
}