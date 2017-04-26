package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.di.module.ProductEditModule;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.view.fragment.ProductEditFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/21/17.
 */
@ProductAddScope
@Component(modules = ProductEditModule.class, dependencies = AppComponent.class)
public interface ProductEditComponent extends ProductDraftComponent {
    void inject(ProductEditFragment fragment);
}
