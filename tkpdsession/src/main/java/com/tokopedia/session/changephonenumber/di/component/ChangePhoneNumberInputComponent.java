package com.tokopedia.session.changephonenumber.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.changephonenumber.di.module.ChangePhoneNumberInputModule;
import com.tokopedia.session.changephonenumber.di.scope.ChangePhoneNumberInputScope;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;

import dagger.Component;

/**
 * Created by milhamj on 27/12/17.
 */

@ChangePhoneNumberInputScope
@Component(modules = ChangePhoneNumberInputModule.class, dependencies = AppComponent.class)
public interface ChangePhoneNumberInputComponent {

    void inject(ChangePhoneNumberInputFragment fragment);
}
