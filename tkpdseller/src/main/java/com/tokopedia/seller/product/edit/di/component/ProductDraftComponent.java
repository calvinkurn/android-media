package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.module.ProductDraftModule;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;
import com.tokopedia.seller.product.edit.view.fragment.ProductDraftAddFragment;
import com.tokopedia.seller.product.edit.view.fragment.ProductDraftEditFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/26/17.
 */

@ProductAddScope
@Component(modules = ProductDraftModule.class, dependencies = ProductComponent.class)
public interface ProductDraftComponent extends ProductAddComponent {
    void inject(ProductDraftAddFragment fragment);
    void inject(ProductDraftEditFragment fragment);
}
