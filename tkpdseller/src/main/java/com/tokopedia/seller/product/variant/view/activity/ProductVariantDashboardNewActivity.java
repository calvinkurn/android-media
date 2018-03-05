package com.tokopedia.seller.product.variant.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDashboardNewFragment;

import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantDashboardNewActivity extends BaseSimpleActivity {

    public static final String EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST = "EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST";
    public static final String EXTRA_PRODUCT_VARIANT_SELECTION = "EXTRA_PRODUCT_VARIANT_SELECTION";
    public static final String EXTRA_CURRENCY_TYPE = "EXTRA_CURR_TYPE";
    public static final String EXTRA_DEFAULT_PRICE = "EXTRA_PRICE";
    public static final String EXTRA_STOCK_TYPE = "EXTRA_STOCK_TYPE";
    public static final String EXTRA_IS_OFFICIAL_STORE = "EXTRA_IS_OFFICIAL_STORE";

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDashboardNewFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        if (getFragment() != null && getFragment() instanceof ProductVariantDashboardNewFragment) {
            if (!validateVariantPrice()) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PRODUCT_VARIANT_SELECTION,
                    ((ProductVariantDashboardNewFragment) getFragment()).getProductVariantViewModelGenerateTid());
            setResult(RESULT_OK, intent);
            this.finish();
        } else {
            super.onBackPressed();
        }
    }

    private boolean validateVariantPrice() {
        ProductVariantViewModel productVariantViewModel =
                ((ProductVariantDashboardNewFragment) getFragment()).getProductVariantViewModel();
        if (productVariantViewModel == null) {
            return true;
        }
        if (!productVariantViewModel.hasSelectedVariant()) {
            return true;
        }
        List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList =
                productVariantViewModel.getProductVariant();

        for (int i = 0, sizei = productVariantCombinationViewModelList.size(); i < sizei; i++) {
            ProductVariantCombinationViewModel productVariantCombinationViewModel = productVariantCombinationViewModelList.get(i);
            if (productVariantCombinationViewModel.getPriceVar() == 0) {
                NetworkErrorHelper.showRedCloseSnackbar(this, getString(R.string.product_variant_price_must_be_filled));
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}