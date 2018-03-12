package com.tokopedia.seller.product.variant.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDashboardFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantDashboardActivity extends BaseSimpleActivity
        implements ProductVariantDashboardFragment.OnProductVariantDashboardFragmentListener {

    public static final String EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST = "EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST";
    public static final String EXTRA_PRODUCT_VARIANT_SELECTION = "EXTRA_PRODUCT_VARIANT_SELECTION";
    public static final String EXTRA_CURRENCY_TYPE = "EXTRA_CURR_TYPE";
    public static final String EXTRA_DEFAULT_PRICE = "EXTRA_PRICE";
    public static final String EXTRA_STOCK_TYPE = "EXTRA_STOCK_TYPE";
    public static final String EXTRA_IS_OFFICIAL_STORE = "EXTRA_IS_OFFICIAL_STORE";
    public static final String EXTRA_NEED_RETAIN_IMAGE = "EXTRA_NEED_RETAIN_IMAGE";
    public static final String EXTRA_DEFAULT_SKU = "EXTRA_DEFAULT_SKU";
    public static final String EXTRA_PRODUCT_SIZECHART = "EXTRA_SIZECHART";
    public static final String EXTRA_HAS_ORIGINAL_VARIANT_LV1 = "EXTRA_HAS_ORI_VAR_LV1";
    public static final String EXTRA_HAS_ORIGINAL_VARIANT_LV2 = "EXTRA_HAS_ORI_VAR_LV2";

    public static Intent getIntent(Context context, ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
                                   ProductVariantViewModel productVariantViewModel, @CurrencyTypeDef int currencyType,
                                   double defaultPrice, @StockTypeDef int stockType, boolean isOfficialStore, String defaultSku,
                                   boolean needRetainImage, ProductPictureViewModel productSizeChart,
                                   boolean hasOriVarLv1, boolean hasOriVarLv2) {
        Intent intent = new Intent(context, ProductVariantDashboardActivity.class);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, productVariantByCatModelList);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_SELECTION, productVariantViewModel);
        intent.putExtra(EXTRA_CURRENCY_TYPE, currencyType);
        intent.putExtra(EXTRA_DEFAULT_PRICE, defaultPrice);
        intent.putExtra(EXTRA_STOCK_TYPE, stockType);
        intent.putExtra(EXTRA_IS_OFFICIAL_STORE, isOfficialStore);
        intent.putExtra(EXTRA_DEFAULT_SKU, defaultSku);
        intent.putExtra(EXTRA_NEED_RETAIN_IMAGE, needRetainImage);
        intent.putExtra(EXTRA_PRODUCT_SIZECHART, productSizeChart);
        intent.putExtra(EXTRA_HAS_ORIGINAL_VARIANT_LV1, hasOriVarLv1);
        intent.putExtra(EXTRA_HAS_ORIGINAL_VARIANT_LV2, hasOriVarLv2);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDashboardFragment.newInstance();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    public void onProductVariantSaved(ProductVariantViewModel productVariantViewModel,
                                      ProductPictureViewModel productPictureViewModel) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRODUCT_VARIANT_SELECTION, productVariantViewModel);
        intent.putExtra(EXTRA_PRODUCT_SIZECHART, productPictureViewModel);
        setResult(RESULT_OK, intent);
        this.finish();
    }
}