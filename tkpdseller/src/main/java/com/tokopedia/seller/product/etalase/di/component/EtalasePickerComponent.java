package com.tokopedia.seller.product.etalase.di.component;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.etalase.di.module.EtalasePickerModule;
import com.tokopedia.seller.product.etalase.di.scope.EtalasePickerScope;
import com.tokopedia.seller.product.etalase.view.fragment.EtalasePickerFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/5/17.
 */
@EtalasePickerScope
@Component(modules = EtalasePickerModule.class, dependencies = ProductComponent.class)
public interface EtalasePickerComponent {
    void inject(EtalasePickerFragment etalasePickerFragment);
}
