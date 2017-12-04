package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.centralizedotp.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.centralizedotp.fragment.VerificationFragment;
import com.tokopedia.session.login.loginphonenumber.fragment.ChooseTokocashAccountFragment;
import com.tokopedia.session.login.loginphonenumber.fragment.LoginPhoneNumberFragment;
import com.tokopedia.session.login.loginphonenumber.fragment.NotConnectedTokocashFragment;

import dagger.Component;

/**
 * @author by nisie on 10/10/17.
 */

@SessionScope
@Component(modules = SessionModule.class, dependencies = AppComponent.class)
public interface SessionComponent {
    void inject(LoginPhoneNumberFragment loginPhoneNumberFragment);

    void inject(VerificationFragment verificationFragment);

    void inject(ChooseVerificationMethodFragment selectVerificationMethodFragment);

    void inject(ChooseTokocashAccountFragment chooseTokocashAccountFragment);

    void inject(NotConnectedTokocashFragment notConnectedTokocashFragment);

}
