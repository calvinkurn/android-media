package com.tokopedia.seller.product.manage.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.view.fragment.ManageProductFragment;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ManageProductActivity extends BaseSimpleActivity implements HasComponent<ProductComponent> {
    @Override
    protected Fragment getNewFragment() {
        return new ManageProductFragment();
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter)getApplication()).getProductComponent();
    }
}
