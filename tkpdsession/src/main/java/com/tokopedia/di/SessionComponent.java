package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.phoneverification.view.fragment.ChangePhoneNumberFragment;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionPhoneVerificationFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberEmailVerificationFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;
import com.tokopedia.session.register.registerphonenumber.view.fragment.AddNameRegisterPhoneFragment;
import com.tokopedia.session.register.view.fragment.CreatePasswordFragment;
import com.tokopedia.user.session.UserSession;

import dagger.Component;

/**
 * @author by nisie on 10/10/17.
 */

@SessionScope
@Component(modules = SessionModule.class, dependencies = AppComponent.class)
public interface SessionComponent {

    void inject(CreatePasswordFragment createPasswordFragment);

    void inject(ChangePhoneNumberFragment changePhoneNumberFragment);

    void inject(PhoneVerificationFragment phoneVerificationFragment);

    void inject(ProfileCompletionPhoneVerificationFragment profileCompletionPhoneVerificationFragment);

    void inject(ChangePhoneNumberInputFragment fragment);

    void inject(ChangePhoneNumberWarningFragment fragment);

    void inject(ChangePhoneNumberEmailVerificationFragment fragment);

    void inject(AddNameRegisterPhoneFragment addNameFragment);

    GetUserInfoUseCase getUserInfoUseCase();

    UserSession userSession();
}
