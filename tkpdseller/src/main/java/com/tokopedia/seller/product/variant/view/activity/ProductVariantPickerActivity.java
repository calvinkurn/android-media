package com.tokopedia.seller.product.variant.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.base.view.listener.BasePickerItemCacheList;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerCacheFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchFragment;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantPickerActivity extends BasePickerMultipleItemActivity<ProductVariantViewModel> implements HasComponent<ProductComponent> {

    public static final String EXTRA_INTENT_SEARCH_ITEM_LIST = "EXTRA_INTENT_SEARCH_ITEM_LIST";

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        showBottomSheetInfo(false);
    }

    @Override
    protected Fragment getSearchListFragment() {
        return new ProductVariantPickerSearchFragment();
    }

    @Override
    protected Fragment getCacheListFragment() {
        return new ProductVariantPickerCacheFragment();
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }

    @Override
    public void removeItemFromSearch(ProductVariantViewModel productVariantViewModel) {
        super.removeItemFromSearch(productVariantViewModel);
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

    private void updateBottomSheetInfo() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CONTAINER_CACHE_LIST_TAG);
        List<ProductVariantViewModel> selectedItemList = ((BasePickerItemCacheList<ProductVariantViewModel>) fragment).getItemList();
        bottomSheetTitleTextView.setText(getString(R.string.product_variant_item_picker_selected_title, String.valueOf(selectedItemList.size()), "Warna"));
        if (selectedItemList.size() > 0) {
            showBottomSheetInfo(true);
        } else {
            showBottomSheetInfo(false);
        }
    }
}