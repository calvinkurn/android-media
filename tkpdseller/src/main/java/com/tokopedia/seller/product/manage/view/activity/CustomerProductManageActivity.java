package com.tokopedia.seller.product.manage.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSellerFragment;

/**
 * @author okasurya on 8/27/18.
 */
public class CustomerProductManageActivity extends BaseSimpleActivity implements HasComponent<ProductComponent> {

    @DeepLink(ApplinkConst.PRODUCT_MANAGE)
    public static Intent newInstance(Context context, Bundle bundle) {
        if(GlobalConfig.isCustomerApp()) {
            return new Intent(context, CustomerProductManageActivity.class).putExtras(bundle);
        } else {
            return new Intent(context, ProductManageActivity.class).putExtras(bundle);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return new ProductManageSellerFragment();
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }
}
