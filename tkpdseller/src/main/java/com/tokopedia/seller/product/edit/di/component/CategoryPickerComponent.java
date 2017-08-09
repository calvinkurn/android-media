package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.module.CategoryPickerModule;
import com.tokopedia.seller.product.edit.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.category.view.fragment.CategoryPickerFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerScope
@Component (modules = CategoryPickerModule.class, dependencies = ProductComponent.class)
public interface CategoryPickerComponent {
    void inject(CategoryPickerFragment categoryPickerFragment);
}
