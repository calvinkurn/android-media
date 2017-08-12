package com.tokopedia.seller.product.variant.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerCacheFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchFragment;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAddCreditFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantPickerActivity extends BasePickerMultipleItemActivity<ProductVariantViewModel> implements HasComponent<ProductComponent> {

    @Override
    protected Fragment getSearchListFragment() {
        return new ProductVariantPickerSearchFragment();
    }

    @Override
    protected Fragment getCacheListFragment() {
        return new ProductVariantPickerCacheFragment();
    }

    @Override
    protected void initialExtra(List<ProductVariantViewModel> itemPickerTypeList) {
        ArrayList<ProductVariantViewModel> viewModelList = getIntent().getParcelableArrayListExtra(EXTRA_INTENT_PICKER_ITEM_LIST);
        for (ProductVariantViewModel productVariantViewModel: viewModelList) {
            itemPickerTypeList.add(productVariantViewModel);
        }
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent(getActivityModule());
    }
}