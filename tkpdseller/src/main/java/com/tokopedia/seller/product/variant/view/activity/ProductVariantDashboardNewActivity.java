package com.tokopedia.seller.product.variant.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDashboardFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDashboardNewFragment;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantDashboardNewActivity extends BaseSimpleActivity {

    public static final String EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST = "EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST";
    public static final String EXTRA_PRODUCT_VARIANT_SELECTION = "EXTRA_PRODUCT_VARIANT_SELECTION";
    public static final String EXTRA_CURRENCY_TYPE = "EXTRA_CURR_TYPE";

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDashboardNewFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        if (getFragment()!= null && getFragment() instanceof ProductVariantDashboardNewFragment) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PRODUCT_VARIANT_SELECTION,
                    ((ProductVariantDashboardNewFragment) getFragment()).getProductVariantViewModelGenerateTid());
            setResult(RESULT_OK, intent);
            this.finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}