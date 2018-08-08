package com.tokopedia.seller.product.picker.di;

import com.tokopedia.seller.product.picker.view.ProductListPickerSearchFragment;
import com.tokopedia.seller.product.common.di.component.ProductComponent;

import dagger.Component;

/**
 * Created by zulfikarrahman on 9/8/17.
 */

@ProductListScope
@Component(modules = ProductListModule.class, dependencies = ProductComponent.class)
public interface ProductListComponent {
    void inject(ProductListPickerSearchFragment productListPickerSearchFragment);
}
