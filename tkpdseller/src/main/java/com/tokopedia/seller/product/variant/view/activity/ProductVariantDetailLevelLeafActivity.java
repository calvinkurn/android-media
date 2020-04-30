package com.tokopedia.seller.product.variant.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.common.util.StockTypeDef;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDetailLeafFragment;

/**
 * Created by hendry on 8/2/17.
 */

public class ProductVariantDetailLevelLeafActivity extends BaseSimpleActivity implements
        ProductVariantDetailLeafFragment.OnProductVariantDetailLeafFragmentListener {

    public static final String EXTRA_PRODUCT_VARIANT_LEAF_DATA = "var_data";
    public static final String EXTRA_PRODUCT_VARIANT_OPTION_CHILD = "opt_child";
    public static final String EXTRA_PRODUCT_VARIANT_NAME = "var_name";
    public static final String EXTRA_CURRENCY_TYPE = "curr_type";
    public static final String EXTRA_NEED_RETAIN_IMAGE = "need_retain_img";
    public static final String EXTRA_STOCK_TYPE = "stock_typ";
    public static final String EXTRA_IS_OFFICIAL_STORE = "is_off_store";
    public static final String EXTRA_HAS_WHOLESALE = "has_wholesale";
    public static final String EXTRA_IS_ADD = "EXTRA_IS_ADD";
    public static final String EXTRA_IS_ADD_EDIT = "EXTRA_IS_ADD_EDIT";

    public static final String EXTRA_ACTION_SUBMIT = "sbmt";

    public static final String SAVED_HAS_LEAF_CHANGED = "svd_leaf_chg";

    public static final int VARIANT_EDIT_LEAF_REQUEST_CODE = 906;

    private ProductVariantCombinationViewModel productVariantCombinationViewModel;
    private String variantName;
    ProductVariantOptionChild productVariantOptionChild;

    private @CurrencyTypeDef
    int currencyType;
    private boolean needRetainImage;
    private boolean imageChanged;
    private @StockTypeDef
    int stockType;
    private boolean isOfficialStore;
    private boolean hasWholesale;
    private boolean isAddStatus;
    private boolean isAddEdit;

    public static void start(Context context, Fragment fragment,
                             ProductVariantCombinationViewModel productVariantCombinationViewModel,
                             ProductVariantOptionChild productVariantOptionChild,
                             String variantName, @CurrencyTypeDef int currencyType,
                             @StockTypeDef int stockType, boolean isOfficialStore,
                             boolean needRetainImage, boolean hasWholesale, boolean isAddStatus, boolean isAddEdit) {
        Intent intent = getIntent(context, productVariantCombinationViewModel, productVariantOptionChild,
                variantName, currencyType, stockType, isOfficialStore, needRetainImage, hasWholesale,isAddStatus, isAddEdit);
        fragment.startActivityForResult(intent, VARIANT_EDIT_LEAF_REQUEST_CODE);
    }

    public static void start(Activity activity,
                             ProductVariantCombinationViewModel productVariantCombinationViewModel,
                             ProductVariantOptionChild productVariantOptionChild,
                             String variantName, @CurrencyTypeDef int currencyType,
                             @StockTypeDef int stockType, boolean isOfficialStore,
                             boolean needRetainImage, boolean hasWholesale, boolean isAddStatus) {
        Intent intent = getIntent(activity, productVariantCombinationViewModel, productVariantOptionChild,
                variantName, currencyType, stockType, isOfficialStore, needRetainImage, hasWholesale, isAddStatus, false);
        activity.startActivityForResult(intent, VARIANT_EDIT_LEAF_REQUEST_CODE);
    }

    public static Intent getIntent(Context context,
                                   ProductVariantCombinationViewModel productVariantCombinationViewModel,
                                   ProductVariantOptionChild productVariantOptionChild,
                                   String variantName, @CurrencyTypeDef int currencyType,
                                   @StockTypeDef int stockType, boolean isOfficialStore,
                                   boolean needRetainImage, boolean hasWholesale,boolean isAddStatus, boolean isAddEdit) {
        Intent intent = new Intent(context, ProductVariantDetailLevelLeafActivity.class);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_LEAF_DATA, productVariantCombinationViewModel);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_OPTION_CHILD, productVariantOptionChild);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_NAME, variantName);
        intent.putExtra(EXTRA_CURRENCY_TYPE, currencyType);
        intent.putExtra(EXTRA_NEED_RETAIN_IMAGE, needRetainImage);
        intent.putExtra(EXTRA_STOCK_TYPE, stockType);
        intent.putExtra(EXTRA_IS_OFFICIAL_STORE, isOfficialStore);
        intent.putExtra(EXTRA_HAS_WHOLESALE, hasWholesale);
        intent.putExtra(EXTRA_IS_ADD, isAddStatus);
        intent.putExtra(EXTRA_IS_ADD_EDIT, isAddEdit);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        variantName = intent.getStringExtra(EXTRA_PRODUCT_VARIANT_NAME);
        currencyType = intent.getIntExtra(EXTRA_CURRENCY_TYPE, CurrencyTypeDef.TYPE_IDR);

        if (savedInstanceState == null) {
            productVariantCombinationViewModel = intent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_LEAF_DATA);
            productVariantOptionChild = intent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_OPTION_CHILD);
        } else {
            productVariantCombinationViewModel = savedInstanceState.getParcelable(EXTRA_PRODUCT_VARIANT_LEAF_DATA);
            productVariantOptionChild = savedInstanceState.getParcelable(EXTRA_PRODUCT_VARIANT_OPTION_CHILD);
            imageChanged = savedInstanceState.getBoolean(SAVED_HAS_LEAF_CHANGED, false);
        }
        needRetainImage = intent.getBooleanExtra(EXTRA_NEED_RETAIN_IMAGE, false);
        stockType = intent.getIntExtra(EXTRA_STOCK_TYPE, StockTypeDef.TYPE_ACTIVE);
        hasWholesale  = intent.getBooleanExtra(EXTRA_HAS_WHOLESALE, false);
        isOfficialStore = intent.getBooleanExtra(EXTRA_IS_OFFICIAL_STORE, false);
        isAddStatus = intent.getBooleanExtra(EXTRA_IS_ADD, false);
        isAddEdit = intent.getBooleanExtra(EXTRA_IS_ADD_EDIT, false);

        super.onCreate(savedInstanceState);
    }

    @Override
    public String getVariantName() {
        return variantName;
    }

    @Override
    public int getCurrencyTypeDef() {
        return currencyType;
    }

    public boolean needRetainImage() {
        return needRetainImage;
    }

    @Override
    public void onImageChanged() {
        imageChanged = true;
    }

    @Override
    public int getStockType() {
        return stockType;
    }

    @Override
    public boolean isOfficialStore() {
        return isOfficialStore;
    }

    @Override
    public boolean isAddStatus() {
        return isAddStatus;
    }

    @Override
    public boolean isAddEdit() {
        return isAddEdit;
    }

    @Override
    public boolean hasWholesale() {
        return hasWholesale;
    }

    @Override
    public ProductVariantOptionChild getProductVariantChild() {
        return productVariantOptionChild;
    }

    @Override
    public ProductVariantCombinationViewModel getProductVariantCombinationViewModel() {
        return productVariantCombinationViewModel;
    }

    @Override
    public void onSubmitVariant() {
        Intent intent = new Intent(EXTRA_ACTION_SUBMIT);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_LEAF_DATA, productVariantCombinationViewModel);
        if (imageChanged) {
            intent.putExtra(EXTRA_PRODUCT_VARIANT_OPTION_CHILD, productVariantOptionChild);
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDetailLeafFragment.newInstance();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_VARIANT_LEAF_DATA, productVariantCombinationViewModel);
        outState.putParcelable(EXTRA_PRODUCT_VARIANT_OPTION_CHILD, productVariantOptionChild);
        outState.putBoolean(SAVED_HAS_LEAF_CHANGED, imageChanged);
    }
}