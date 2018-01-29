package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;
import com.tokopedia.otp.securityquestion.view.fragment.SecurityQuestionFragment;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberEmailVerificationFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;
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

    void inject(ChangePhoneNumberInputFragment fragment);

    void inject(VerificationActivity loginFragment);

    void inject(VerificationFragment verificationFragment);

    void inject(ChooseVerificationMethodFragment chooseVerificationMethodFragment);

    void inject(ChangePhoneNumberWarningFragment fragment);

    void inject(ChangePhoneNumberEmailVerificationFragment fragment);

    void inject(LoginPhoneNumberFragment loginPhoneNumberFragment);

    void inject(com.tokopedia.otp.tokocashotp.view.fragment.VerificationFragment verificationFragment);

    void inject(com.tokopedia.otp.tokocashotp.view.fragment.ChooseVerificationMethodFragment
                        selectVerificationMethodFragment);

    void inject(ChooseTokocashAccountFragment chooseTokocashAccountFragment);

    void inject(NotConnectedTokocashFragment notConnectedTokocashFragment);

    void inject(SecurityQuestionFragment securityQuestionFragment);

    GetUserInfoUseCase getUserInfoUseCase();

}
