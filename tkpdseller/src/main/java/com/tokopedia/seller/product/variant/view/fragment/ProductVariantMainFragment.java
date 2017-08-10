package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantMainView;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantMainFragment extends BaseDaggerFragment implements ProductVariantMainView {

    public static ProductVariantMainFragment newInstance(String keyword, long departmentId, long selectedCatalogID) {
        Bundle args = new Bundle();
        ProductVariantMainFragment fragment = new ProductVariantMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}