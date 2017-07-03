package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.seller.product.di.module.ProductDraftListCountModule;
import com.tokopedia.seller.product.di.scope.ProductAddScope;

import dagger.Component;

/**
 * Created by hendry on 6/21/2017.
 */

@ProductAddScope
@Component(modules = ProductDraftListCountModule.class, dependencies = AppComponent.class)
public interface ProductDraftListCountComponent {
    void inject(ManageProduct manageProduct);
}
