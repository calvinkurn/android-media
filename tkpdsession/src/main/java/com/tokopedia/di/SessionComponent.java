package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.tokocashotp.view.fragment.ChooseTokocashVerificationMethodFragment;
import com.tokopedia.otp.tokocashotp.view.fragment.TokoCashVerificationFragment;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.login.loginemail.view.fragment.LoginFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.ChooseTokocashAccountFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.LoginPhoneNumberFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.NotConnectedTokocashFragment;
import com.tokopedia.session.register.registerphonenumber.view.fragment.AddNameRegisterPhoneFragment;
import com.tokopedia.session.register.registerphonenumber.view.fragment.RegisterPhoneNumberFragment;
import com.tokopedia.session.register.view.fragment.CreatePasswordFragment;
import com.tokopedia.session.register.view.fragment.RegisterEmailFragment;
import com.tokopedia.session.register.view.fragment.RegisterInitialFragment;
import com.tokopedia.user.session.UserSession;

import dagger.Component;

/**
 * @author by nisie on 10/10/17.
 */

@SessionScope
@Component(modules = SessionModule.class, dependencies = AppComponent.class)
public interface SessionComponent {

    void inject(LoginFragment loginFragment);

    void inject(RegisterInitialFragment registerInitialFragment);

    void inject(RegisterEmailFragment registerEmailFragment);

    void inject(CreatePasswordFragment createPasswordFragment);

    void inject(LoginPhoneNumberFragment loginPhoneNumberFragment);

    void inject(TokoCashVerificationFragment verificationFragment);

    void inject(ChooseTokocashVerificationMethodFragment selectVerificationMethodFragment);

    void inject(ChooseTokocashAccountFragment chooseTokocashAccountFragment);

    void inject(NotConnectedTokocashFragment notConnectedTokocashFragment);

    void inject(RegisterPhoneNumberFragment registerPhoneNumberFragment);

    void inject(AddNameRegisterPhoneFragment addNameFragment);

    GetUserInfoUseCase getUserInfoUseCase();

    UserSession userSession();
}
