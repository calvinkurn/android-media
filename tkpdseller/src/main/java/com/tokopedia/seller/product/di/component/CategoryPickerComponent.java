package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.di.module.CategoryPickerModule;
import com.tokopedia.seller.product.di.scope.CategoryPickerScope;

import dagger.Component;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerScope
@Component (modules = CategoryPickerModule.class, dependencies = AppComponent.class)
public interface CategoryPickerComponent {
}
