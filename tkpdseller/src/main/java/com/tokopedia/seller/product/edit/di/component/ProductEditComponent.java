package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.module.ProductEditModule;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;
import com.tokopedia.seller.product.edit.view.fragment.ProductDuplicateFragment;
import com.tokopedia.seller.product.edit.view.fragment.ProductEditFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/21/17.
 */
@ProductAddScope
@Component(modules = ProductEditModule.class, dependencies = ProductComponent.class)
public interface ProductEditComponent extends ProductDraftComponent {
    void inject(ProductEditFragment fragment);

    void inject(ProductDuplicateFragment fragment);
}
