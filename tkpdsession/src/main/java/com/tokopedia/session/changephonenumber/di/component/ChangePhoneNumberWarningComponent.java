package com.tokopedia.session.changephonenumber.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.changephonenumber.di.module.ChangePhoneNumberWarningModule;
import com.tokopedia.session.changephonenumber.di.scope.ChangePhoneNumberWarningScope;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;

import dagger.Component;

/**
 * Created by milhamj on 27/12/17.
 */

@ChangePhoneNumberWarningScope
@Component(modules = ChangePhoneNumberWarningModule.class, dependencies = AppComponent.class)
public interface ChangePhoneNumberWarningComponent {

    void inject(ChangePhoneNumberWarningFragment fragment);
}
