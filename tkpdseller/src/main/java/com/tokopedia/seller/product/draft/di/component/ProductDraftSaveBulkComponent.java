package com.tokopedia.seller.product.draft.di.component;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.draft.di.module.ProductDraftSaveBulkModule;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;

import dagger.Component;

/**
 * Created by hendry on 6/21/2017.
 */

@ProductAddScope
@Component(modules = ProductDraftSaveBulkModule.class, dependencies = ProductComponent.class)
public interface ProductDraftSaveBulkComponent {
    void inject(ProductDraftListActivity productDraftListActivity);
}
