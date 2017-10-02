package com.tokopedia.seller.product.manage.di;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.view.fragment.ProductFragmentManage;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSortFragmentManageSort;

import dagger.Component;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@ProductManageScope
@Component(modules = ProductManageModule.class, dependencies = ProductComponent.class)
public interface ProductManageComponent {
    void inject(ProductFragmentManage productManageFragment);

    void inject(ProductManageSortFragmentManageSort productManageSortFragment);
}
