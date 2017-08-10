package com.tokopedia.seller.product.variant.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantMainFragment;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantMainActivity extends BaseSimpleActivity implements HasComponent<ProductComponent> {

    @Override
    protected Fragment getNewFragment() {
        return new ProductVariantMainFragment();
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent(getActivityModule());
    }
}
