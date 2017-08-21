package com.tokopedia.seller.product.variant.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerCacheFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchFragment;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerMultipleItem;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantPickerActivity extends BasePickerMultipleItemActivity<ProductVariantViewModel> implements ProductVariantPickerMultipleItem<ProductVariantViewModel> {

    private ProductVariantByCatModel productVariantByCatModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productVariantByCatModel = getIntent().getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_CATEGORY);
        updateBottomSheetInfo();
    }

    @Override
    protected Fragment getInitialSearchListFragment() {
        return new ProductVariantPickerSearchFragment();
    }

    @Override
    protected Fragment getInitialCacheListFragment() {
        return new ProductVariantPickerCacheFragment();
    }

    @Override
    public void removeItemFromSearch(ProductVariantViewModel productVariantViewModel) {
        super.removeItemFromSearch(productVariantViewModel);
        updateBottomSheetInfo();
    }

    @Override
    public void removeAllItemFromSearch() {
        ((ProductVariantPickerCacheFragment) getCacheListFragment()).removeAllItem();
        updateBottomSheetInfo();
    }

    @Override
    public void addItemFromSearch(ProductVariantViewModel productVariantViewModel) {
        super.addItemFromSearch(productVariantViewModel);
        updateBottomSheetInfo();
    }

    @Override
    public void removeItemFromCache(ProductVariantViewModel productVariantViewModel) {
        super.removeItemFromCache(productVariantViewModel);
        updateBottomSheetInfo();
    }

    @Override
    protected Intent getDefaultIntentResult() {
        VariantUnitSubmit variantUnitSubmit = new VariantUnitSubmit();
        variantUnitSubmit.setVariantId(productVariantByCatModel.getVariantId());
        variantUnitSubmit.setVariantUnitId(((ProductVariantPickerSearchFragment) getSearchListFragment()).getCurrentUnitId());
        variantUnitSubmit.setPosition(ProductVariantUtils.getVariantPositionByStatus(productVariantByCatModel.getStatus()));
        variantUnitSubmit.setVariantSubmitOptionList(((ProductVariantPickerCacheFragment) getCacheListFragment()).getVariantSubmitOptionList());
        Intent intent = new Intent();
        intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_UNIT_SUBMIT, variantUnitSubmit);
        return intent;
    }

    private void updateBottomSheetInfo() {
        Fragment fragment = getCacheListFragment();
        int selectedItemSize = 0;
        if ((fragment) != null) {
            selectedItemSize = ((ProductVariantPickerCacheFragment) fragment).getItemList().size();
        }
        String variantCategoryName = "";
        if (productVariantByCatModel != null && !TextUtils.isEmpty(productVariantByCatModel.getName())) {
            variantCategoryName = productVariantByCatModel.getName();
        }
        bottomSheetTitleTextView.setText(getString(R.string.product_variant_item_picker_selected_title,
                String.valueOf(selectedItemSize), variantCategoryName));
        if (selectedItemSize > 0) {
            showBottomSheetInfo(true);
        } else {
            showBottomSheetInfo(false);
        }
    }
}