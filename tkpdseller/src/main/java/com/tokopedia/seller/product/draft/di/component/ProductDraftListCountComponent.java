package com.tokopedia.seller.product.draft.di.component;

import com.tokopedia.product.manage.list.di.ProductManageScope;
import com.tokopedia.product.manage.list.view.fragment.ProductManageSellerFragment;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.seller.product.draft.di.module.ProductDraftListCountModule;

import dagger.Component;

/**
 * Created by hendry on 6/21/2017.
 */

@ProductManageScope
@Component(modules = ProductDraftListCountModule.class, dependencies = ProductComponent.class)
public interface ProductDraftListCountComponent {
    void inject(ProductManageSellerFragment productManageSellerFragment);
}
