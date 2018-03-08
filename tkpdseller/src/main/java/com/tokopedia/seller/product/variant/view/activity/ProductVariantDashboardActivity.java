package com.tokopedia.seller.product.variant.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDashboardFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantDashboardActivity extends BaseSimpleActivity {

    public static final String EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST = "EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST";
    public static final String EXTRA_PRODUCT_VARIANT_SELECTION = "EXTRA_PRODUCT_VARIANT_SELECTION";
    public static final String EXTRA_CURRENCY_TYPE = "EXTRA_CURR_TYPE";
    public static final String EXTRA_DEFAULT_PRICE = "EXTRA_PRICE";
    public static final String EXTRA_STOCK_TYPE = "EXTRA_STOCK_TYPE";
    public static final String EXTRA_IS_OFFICIAL_STORE = "EXTRA_IS_OFFICIAL_STORE";
    public static final String EXTRA_NEED_RETAIN_IMAGE = "EXTRA_NEED_RETAIN_IMAGE";
    public static final String EXTRA_DEFAULT_SKU = "EXTRA_DEFAULT_SKU";

    public static Intent getIntent(Context context, ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
                                   ProductVariantViewModel productVariantViewModel, @CurrencyTypeDef int currencyType,
                                   double defaultPrice, @StockTypeDef int stockType, boolean isOfficialStore, String defaultSku,
                                   boolean needRetainImage){
        Intent intent = new Intent(context, ProductVariantDashboardActivity.class);
        intent.putExtra(ProductVariantDashboardActivity.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, productVariantByCatModelList);
        intent.putExtra(ProductVariantDashboardActivity.EXTRA_PRODUCT_VARIANT_SELECTION, productVariantViewModel);
        intent.putExtra(ProductVariantDashboardActivity.EXTRA_CURRENCY_TYPE, currencyType);
        intent.putExtra(ProductVariantDashboardActivity.EXTRA_DEFAULT_PRICE, defaultPrice);
        intent.putExtra(ProductVariantDashboardActivity.EXTRA_STOCK_TYPE, stockType);
        intent.putExtra(ProductVariantDashboardActivity.EXTRA_IS_OFFICIAL_STORE, isOfficialStore);
        intent.putExtra(ProductVariantDashboardActivity.EXTRA_DEFAULT_SKU, defaultSku);
        intent.putExtra(ProductVariantDashboardActivity.EXTRA_NEED_RETAIN_IMAGE, needRetainImage);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDashboardFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        if (getFragment() != null && getFragment() instanceof ProductVariantDashboardFragment) {
            if (!validateVariantPrice()) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PRODUCT_VARIANT_SELECTION,
                    ((ProductVariantDashboardFragment) getFragment()).getProductVariantViewModel());
            setResult(RESULT_OK, intent);
            this.finish();
        } else {
            super.onBackPressed();
        }
    }

    private boolean validateVariantPrice() {
        ProductVariantViewModel productVariantViewModel =
                ((ProductVariantDashboardFragment) getFragment()).getProductVariantViewModel();
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