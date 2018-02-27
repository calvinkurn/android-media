package com.tokopedia.seller.product.variant.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDetailLevel1ListFragment;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardNewViewModel;

import java.util.List;

import static com.tokopedia.seller.product.variant.view.activity.ProductVariantDetailLevelLeafActivity.EXTRA_PRODUCT_VARIANT_LEAF_DATA;

/**
 * Created by hendry on 8/2/17.
 */

public class ProductVariantDetailLevel1ListActivity extends BaseSimpleActivity implements
        ProductVariantDetailLevel1ListFragment.OnProductVariantDataManageFragmentListener {

    public static final String EXTRA_PRODUCT_VARIANT_DATA = "var_data";
    public static final String EXTRA_PRODUCT_VARIANT_LV1_NAME = "var_lv1_nm";
    public static final String EXTRA_PRODUCT_VARIANT_LV2_NAME = "var_lv2_nm";
    public static final String EXTRA_CURRENCY_TYPE = "curr_type";

    public static final String EXTRA_ACTION_SUBMIT = "sbmt";

    public static final int VARIANT_EDIT_LEVEL1_LIST_REQUEST_CODE = 905;

    public static final String SAVED_HAS_LEAF_CHANGED = "svd_leaf_chg";

    private ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel;
    private String varLv1name;
    private String varLv2name;
    private boolean hasLeafChanged;
    private @CurrencyTypeDef
    int currencyType;

    private boolean needRefreshData;

    public static void start(Context context, Fragment fragment,
                             ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel,
                             String variantLv1Name, String variantLv2Name,
                             @CurrencyTypeDef int currencyType) {
        Intent intent = getIntent(context, productVariantDashboardNewViewModel, variantLv1Name, variantLv2Name, currencyType);
        fragment.startActivityForResult(intent, VARIANT_EDIT_LEVEL1_LIST_REQUEST_CODE);
    }

    public static Intent getIntent(Context context,
                                   ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel,
                                   String variantLv1Name, String variantLv2Name, @CurrencyTypeDef int currencyType) {
        Intent intent = new Intent(context, ProductVariantDetailLevel1ListActivity.class);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_DATA, productVariantDashboardNewViewModel);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_LV1_NAME, variantLv1Name);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_LV2_NAME, variantLv2Name);
        intent.putExtra(EXTRA_CURRENCY_TYPE, currencyType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            productVariantDashboardNewViewModel = intent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_DATA);
        } else {
            productVariantDashboardNewViewModel = savedInstanceState.getParcelable(EXTRA_PRODUCT_VARIANT_DATA);
            hasLeafChanged = savedInstanceState.getBoolean(SAVED_HAS_LEAF_CHANGED);
        }
        varLv1name = intent.getStringExtra(EXTRA_PRODUCT_VARIANT_LV1_NAME);
        varLv2name = intent.getStringExtra(EXTRA_PRODUCT_VARIANT_LV2_NAME);
        currencyType = intent.getIntExtra(EXTRA_CURRENCY_TYPE, CurrencyTypeDef.TYPE_IDR);

        super.onCreate(savedInstanceState);

        toolbar.setTitle(getTitle() + " " + productVariantDashboardNewViewModel.getProductVariantOptionChildLv1().getValue());

    }

    @Override
    public void onBackPressed() {
        if (hasLeafChanged) {
            // it has no submit button, so save the model back to caller
            onSubmitVariant();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public List<ProductVariantCombinationViewModel> getProductVariantCombinationViewModelList() {
        return productVariantDashboardNewViewModel.getProductVariantCombinationViewModelList();
    }

    @Override
    public String getVariantName() {
        return varLv1name;
    }

    @Override
    public String getVariantValue() {
        return productVariantDashboardNewViewModel.getProductVariantOptionChildLv1().getValue();
    }

    @Override
    public int getCurrencyType() {
        return currencyType;
    }

    @Override
    public void onSubmitVariant() {
        Intent intent = new Intent(EXTRA_ACTION_SUBMIT);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_DATA, productVariantDashboardNewViewModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDetailLevel1ListFragment.newInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProductVariantDetailLevelLeafActivity.VARIANT_EDIT_LEAF_REQUEST_CODE) {
            if (data!=null && data.hasExtra(EXTRA_PRODUCT_VARIANT_LEAF_DATA)) {
                ProductVariantCombinationViewModel productVariantCombinationViewModel =
                        data.getParcelableExtra(EXTRA_PRODUCT_VARIANT_LEAF_DATA);
                this.productVariantDashboardNewViewModel.replaceSelectedVariantFor(productVariantCombinationViewModel);
                this.hasLeafChanged = true;
                needRefreshData = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefreshData) {
            ((ProductVariantDetailLevel1ListFragment)
                    getSupportFragmentManager().findFragmentByTag(getTagFragment())).refreshData();
            needRefreshData = false;
        }
    }

    @Override
    public void goToLeaf(ProductVariantCombinationViewModel productVariantCombinationViewModel) {
        ProductVariantDetailLevelLeafActivity.start(this, productVariantCombinationViewModel, varLv2name, currencyType);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_VARIANT_DATA, productVariantDashboardNewViewModel);
        outState.putBoolean(SAVED_HAS_LEAF_CHANGED, hasLeafChanged);
    }
}