package com.tokopedia.seller.product.category.di.component;

import com.tokopedia.core.common.category.di.module.CategoryPickerModule;
import com.tokopedia.core.common.category.di.scope.CategoryPickerScope;
import com.tokopedia.seller.manageitem.di.component.ProductComponent;
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
