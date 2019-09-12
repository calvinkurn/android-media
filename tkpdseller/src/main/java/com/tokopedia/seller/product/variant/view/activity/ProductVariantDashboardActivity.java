package com.tokopedia.seller.product.variant.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel;
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.common.util.StockTypeDef;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDashboardFragment;

import java.util.ArrayList;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantDashboardActivity extends BaseSimpleActivity
        implements ProductVariantDashboardFragment.OnProductVariantDashboardFragmentListener {

    public static final String EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST = "EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST";
    public static final String EXTRA_CURRENCY_TYPE = "EXTRA_CURR_TYPE";
    public static final String EXTRA_DEFAULT_PRICE = "EXTRA_PRICE";
    public static final String EXTRA_STOCK_TYPE = "EXTRA_STOCK_TYPE";
    public static final String EXTRA_IS_OFFICIAL_STORE = "EXTRA_IS_OFFICIAL_STORE";
    public static final String EXTRA_NEED_RETAIN_IMAGE = "EXTRA_NEED_RETAIN_IMAGE";
    public static final String EXTRA_DEFAULT_SKU = "EXTRA_DEFAULT_SKU";
    public static final String EXTRA_HAS_ORIGINAL_VARIANT_LV1 = "EXTRA_HAS_ORI_VAR_LV1";
    public static final String EXTRA_HAS_ORIGINAL_VARIANT_LV2 = "EXTRA_HAS_ORI_VAR_LV2";
    public static final String EXTRA_HAS_WHOLESALE = "EXTRA_HAS_WHOLESALE";
    public static final String SAVED_VARIANT_CHANGE_FROM_RESULT = "sdv_var_chg";

    private boolean hasVariantChangedFromResult;

    public static Intent getIntent(Context context, ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
                                   ProductVariantViewModel productVariantViewModel, @CurrencyTypeDef int currencyType,
                                   double defaultPrice, @StockTypeDef int stockType, boolean isOfficialStore, String defaultSku,
                                   boolean needRetainImage, ProductPictureViewModel productSizeChart,
                                   boolean hasOriVarLv1, boolean hasOriVarLv2, boolean hasWholesale) {
        Intent intent = new Intent(context, ProductVariantDashboardActivity.class);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, productVariantByCatModelList);
        intent.putExtra(ProductExtraConstant.EXTRA_PRODUCT_VARIANT_SELECTION, productVariantViewModel);
        intent.putExtra(EXTRA_CURRENCY_TYPE, currencyType);
        intent.putExtra(EXTRA_DEFAULT_PRICE, defaultPrice);
        intent.putExtra(EXTRA_STOCK_TYPE, stockType);
        intent.putExtra(EXTRA_IS_OFFICIAL_STORE, isOfficialStore);
        intent.putExtra(EXTRA_DEFAULT_SKU, defaultSku);
        intent.putExtra(EXTRA_NEED_RETAIN_IMAGE, needRetainImage);
        intent.putExtra(ProductExtraConstant.EXTRA_PRODUCT_SIZECHART, productSizeChart);
        intent.putExtra(EXTRA_HAS_ORIGINAL_VARIANT_LV1, hasOriVarLv1);
        intent.putExtra(EXTRA_HAS_ORIGINAL_VARIANT_LV2, hasOriVarLv2);
        intent.putExtra(EXTRA_HAS_WHOLESALE, hasWholesale);
        return intent;
    }

    @Override
    public void onBackPressed() {
        if (hasVariantChangedFromResult) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.product_dialog_cancel_title))
                    .setMessage(getString(R.string.product_dialog_cancel_message))
                    .setPositiveButton(getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ProductVariantDashboardActivity.super.onBackPressed();
                        }
                    }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // no op, just dismiss
                        }
                    });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!= null) {
            hasVariantChangedFromResult = savedInstanceState.getBoolean(SAVED_VARIANT_CHANGE_FROM_RESULT);
        }
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
        intent.putExtra(ProductExtraConstant.EXTRA_PRODUCT_VARIANT_SELECTION, productVariantViewModel);
        intent.putExtra(ProductExtraConstant.EXTRA_PRODUCT_SIZECHART, productPictureViewModel);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onVariantChangedFromResult() {
        hasVariantChangedFromResult = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_VARIANT_CHANGE_FROM_RESULT, hasVariantChangedFromResult);
    }
}