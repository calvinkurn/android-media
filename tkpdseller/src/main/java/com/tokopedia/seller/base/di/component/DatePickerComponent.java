package com.tokopedia.seller.base.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.base.di.module.DatePickerModule;
import com.tokopedia.seller.base.di.scope.DatePickerScope;
import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;

import dagger.Component;

/**
 * @author sebastianuskh on 4/13/17.
 */
@DatePickerScope
@Component(modules = DatePickerModule.class, dependencies = AppComponent.class)
public interface DatePickerComponent {
    void inject(BaseDatePickerFragment datePickerFragment);

}
