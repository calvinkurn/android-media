package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.InterruptVerificationFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;
import com.tokopedia.otp.phoneverification.view.fragment.ChangePhoneNumberFragment;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.otp.tokocashotp.view.fragment.ChooseTokocashVerificationMethodFragment;
import com.tokopedia.otp.tokocashotp.view.fragment.TokoCashVerificationFragment;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionPhoneVerificationFragment;
import com.tokopedia.session.addchangeemail.view.fragment.AddEmailFragment;
import com.tokopedia.session.addchangeemail.view.fragment.AddEmailVerificationFragment;
import com.tokopedia.session.addchangepassword.view.fragment.AddPasswordFragment;
import com.tokopedia.session.changename.view.fragment.ChangeNameFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberEmailVerificationFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;
import com.tokopedia.session.login.loginemail.view.fragment.LoginFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.ChooseTokocashAccountFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.LoginPhoneNumberFragment;
import com.tokopedia.session.login.loginphonenumber.view.fragment.NotConnectedTokocashFragment;
import com.tokopedia.session.register.registerphonenumber.view.fragment.RegisterPhoneNumberFragment;
import com.tokopedia.session.register.view.fragment.CreatePasswordFragment;
import com.tokopedia.session.register.view.fragment.RegisterEmailFragment;
import com.tokopedia.session.register.view.fragment.RegisterInitialFragment;

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

    void inject(ChangePhoneNumberFragment changePhoneNumberFragment);

    void inject(PhoneVerificationFragment phoneVerificationFragment);

    void inject(ProfileCompletionPhoneVerificationFragment profileCompletionPhoneVerificationFragment);

    void inject(LoginPhoneNumberFragment loginPhoneNumberFragment);

    void inject(VerificationActivity loginFragment);

    void inject(VerificationFragment verificationFragment);

    void inject(InterruptVerificationFragment interruptVerificationFragment);

    void inject(ChooseVerificationMethodFragment chooseVerificationMethodFragment);

    void inject(TokoCashVerificationFragment verificationFragment);

    void inject(ChooseTokocashVerificationMethodFragment selectVerificationMethodFragment);

    void inject(ChangePhoneNumberInputFragment fragment);

    void inject(ChangePhoneNumberWarningFragment fragment);

    void inject(ChangePhoneNumberEmailVerificationFragment fragment);

    void inject(ChooseTokocashAccountFragment chooseTokocashAccountFragment);

    void inject(NotConnectedTokocashFragment notConnectedTokocashFragment);

    void inject(RegisterPhoneNumberFragment registerPhoneNumberFragment);

    void inject(com.tokopedia.otp.registerphonenumber.view.fragment.VerificationFragment verificationFragment);

    void inject(com.tokopedia.otp.registerphonenumber.view.fragment.VerificationMethodFragment verificationMethodFragment);

    void inject(AddEmailFragment addEmailFragment);

    void inject(AddEmailVerificationFragment addEmailVerificationFragment);

    void inject(ChangeNameFragment changeNameFragment);

    void inject(AddPasswordFragment addPasswordFragment);

    GetUserInfoUseCase getUserInfoUseCase();

}
