package com.tokopedia.seller.product.manage.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.view.fragment.ProductFragmentManage;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ProductManageActivity extends BaseSimpleActivity implements HasComponent<ProductComponent> {
    @Override
    protected Fragment getNewFragment() {
        return new ProductFragmentManage();
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter)getApplication()).getProductComponent();
    }
}
