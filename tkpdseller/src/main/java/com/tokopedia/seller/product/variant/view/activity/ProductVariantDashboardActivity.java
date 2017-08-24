package com.tokopedia.seller.product.variant.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDashboardFragment;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantDashboardActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDashboardFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        // TODO hendry to get the variant data and return to the caller
        if (getFragment()!= null && getFragment() instanceof ProductVariantDashboardFragment) {
            Intent intent = new Intent();
            intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_SELECTION,
                    ((ProductVariantDashboardFragment) getFragment()).getVariantData());
            setResult(RESULT_OK, intent);
            this.finish();
        } else {
            super.onBackPressed();
        }
    }
}