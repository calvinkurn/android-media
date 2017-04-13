package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.di.module.ProductAddModule;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Component(modules = ProductAddModule.class, dependencies = AppComponent.class)
public interface ProductAddComponent {
    void inject(ProductAddFragment productAddFragment);
}
