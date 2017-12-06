package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.securityquestion.view.fragment.SecurityQuestionFragment;
import com.tokopedia.otp.tokocashotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.tokocashotp.view.fragment.VerificationFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.ChooseTokocashAccountFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.LoginPhoneNumberFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.NotConnectedTokocashFragment;

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

    void inject(SecurityQuestionFragment securityQuestionFragment);


}
