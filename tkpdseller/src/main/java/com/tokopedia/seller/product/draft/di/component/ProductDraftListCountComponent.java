package com.tokopedia.seller.product.draft.di.component;

import com.tokopedia.seller.product.manage.di.ProductManageScope;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSellerFragment;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.draft.di.module.ProductDraftListCountModule;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;

import dagger.Component;

/**
 * Created by hendry on 6/21/2017.
 */

@ProductManageScope
@Component(modules = ProductDraftListCountModule.class, dependencies = ProductComponent.class)
public interface ProductDraftListCountComponent {
    void inject(ProductManageSellerFragment productManageSellerFragment);
}
