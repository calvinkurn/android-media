package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.centralizedotp.fragment.SelectVerificationMethodFragment;
import com.tokopedia.otp.centralizedotp.fragment.VerificationFragment;
import com.tokopedia.session.login.loginphonenumber.LoginPhoneNumberFragment;

import dagger.Component;

/**
 * @author by nisie on 10/10/17.
 */

@SessionScope
@Component(modules = SessionModule.class, dependencies = AppComponent.class)
public interface SessionComponent {
    void inject(LoginPhoneNumberFragment loginPhoneNumberFragment);

    void inject(VerificationFragment verificationFragment);

    void inject(SelectVerificationMethodFragment selectVerificationMethodFragment);

}
