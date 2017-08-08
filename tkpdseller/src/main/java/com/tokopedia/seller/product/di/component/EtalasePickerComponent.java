package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.di.module.EtalasePickerModule;
import com.tokopedia.seller.product.di.scope.EtalasePickerScope;
import com.tokopedia.seller.product.edit.view.fragment.EtalasePickerFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/5/17.
 */
@EtalasePickerScope
@Component(modules = EtalasePickerModule.class, dependencies = AppComponent.class)
public interface EtalasePickerComponent {
    void inject(EtalasePickerFragment etalasePickerFragment);
}
