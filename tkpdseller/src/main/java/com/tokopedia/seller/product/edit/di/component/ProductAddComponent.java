package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.module.ProductAddModule;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Component(modules = ProductAddModule.class, dependencies = ProductComponent.class)
public interface ProductAddComponent {
    void inject(ProductAddFragment productAddFragment);
}
