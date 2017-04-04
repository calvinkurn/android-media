package com.tokopedia.seller.product.di.component;

import com.tokopedia.seller.product.di.module.CategoryPickerViewModule;
import com.tokopedia.seller.product.di.scope.CategoryPickerViewScope;
import com.tokopedia.seller.product.view.fragment.CategoryPickerFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerViewScope
@Component(modules = CategoryPickerViewModule.class, dependencies = CategoryPickerComponent.class)
public interface CategoryPickerViewComponent {
    void inject(CategoryPickerFragment categoryPickerFragment);
}
