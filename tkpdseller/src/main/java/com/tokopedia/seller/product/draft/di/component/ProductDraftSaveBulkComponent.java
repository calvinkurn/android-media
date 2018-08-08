package com.tokopedia.seller.product.draft.di.component;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope;
import com.tokopedia.seller.product.draft.di.module.ProductDraftSaveBulkModule;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;

import dagger.Component;

/**
 * Created by hendry on 6/21/2017.
 */

@ProductAddScope
@Component(modules = ProductDraftSaveBulkModule.class, dependencies = ProductComponent.class)
public interface ProductDraftSaveBulkComponent {
    void inject(ProductDraftListActivity productDraftListActivity);
}
