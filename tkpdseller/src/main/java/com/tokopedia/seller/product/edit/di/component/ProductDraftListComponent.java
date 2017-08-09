package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.module.ProductDraftListModule;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;
import com.tokopedia.seller.product.draft.view.fragment.ProductDraftListFragment;

import dagger.Component;

/**
 * Created by User on 6/21/2017.
 */

@ProductAddScope
@Component(modules = ProductDraftListModule.class, dependencies = ProductComponent.class)
public interface ProductDraftListComponent {
    void inject(ProductDraftListFragment productDraftListFragment);
}
