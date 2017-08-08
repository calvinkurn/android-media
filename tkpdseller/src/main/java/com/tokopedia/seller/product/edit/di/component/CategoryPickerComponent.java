package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.edit.di.module.CategoryPickerModule;
import com.tokopedia.seller.product.edit.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.edit.view.fragment.CategoryPickerFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerScope
@Component (modules = CategoryPickerModule.class, dependencies = AppComponent.class)
public interface CategoryPickerComponent {
    void inject(CategoryPickerFragment categoryPickerFragment);
}
