package com.tokopedia.seller.product.variant.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerCacheFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchFragment;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAddCreditFragment;

import java.util.UUID;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantPickerActivity extends BasePickerMultipleItemActivity implements HasComponent<ProductComponent> {

    @Override
    public Fragment getSearchListFragment() {
        return new ProductVariantPickerSearchFragment();
    }

    @Override
    public Fragment getCacheListFragment() {
        return new ProductVariantPickerCacheFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
        productVariantViewModel.setId(UUID.randomUUID().toString());
        productVariantViewModel.setTitle(UUID.randomUUID().toString());
        productVariantViewModel.setImageUrl("https://image.flaticon.com/teams/slug/freepik.jpg");
        addItemFromSearch(productVariantViewModel);
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent(getActivityModule());
    }
}