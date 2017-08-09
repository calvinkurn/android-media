package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.module.EtalasePickerModule;
import com.tokopedia.seller.product.edit.di.scope.EtalasePickerScope;
import com.tokopedia.seller.product.edit.view.fragment.EtalasePickerFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/5/17.
 */
@EtalasePickerScope
@Component(modules = EtalasePickerModule.class, dependencies = ProductComponent.class)
public interface EtalasePickerComponent {
    void inject(EtalasePickerFragment etalasePickerFragment);
}
