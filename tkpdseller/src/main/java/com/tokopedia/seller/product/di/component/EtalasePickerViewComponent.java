package com.tokopedia.seller.product.di.component;

import com.tokopedia.seller.product.di.module.EtalasePickerViewModule;
import com.tokopedia.seller.product.di.scope.EtalasePickerViewScope;
import com.tokopedia.seller.product.view.fragment.EtalasePickerFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/5/17.
 */
@EtalasePickerViewScope
@Component(modules = EtalasePickerViewModule.class, dependencies = EtalasePickerComponent.class)
public interface EtalasePickerViewComponent {
    void inject(EtalasePickerFragment etalasePickerFragment);
}
