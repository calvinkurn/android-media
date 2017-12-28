package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.phoneverification.view.fragment.ChangePhoneNumberFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberEmailFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;

import dagger.Component;

/**
 * @author by nisie on 10/10/17.
 */

@SessionScope
@Component(modules = SessionModule.class, dependencies = AppComponent.class)
public interface SessionComponent {

    void inject(ChangePhoneNumberInputFragment fragment);

    void inject(ChangePhoneNumberWarningFragment fragment);

    void inject(ChangePhoneNumberEmailFragment fragment);
}
