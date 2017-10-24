package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.phoneverification.view.fragment.ChangePhoneNumberFragment;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.otp.securityquestion.view.fragment.SecurityQuestionFragment;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionPhoneVerificationFragment;
import com.tokopedia.session.register.view.fragment.CreatePasswordFragment;
import com.tokopedia.session.register.view.fragment.RegisterInitialFragment;

import dagger.Component;

/**
 * @author by nisie on 10/10/17.
 */

@SessionScope
@Component(modules = SessionModule.class, dependencies = AppComponent.class)
public interface SessionComponent {

    void inject(RegisterInitialFragment registerInitialFragment);

    void inject(CreatePasswordFragment createPasswordFragment);

    void inject(SecurityQuestionFragment securityQuestionFragment);

    void inject(ChangePhoneNumberFragment changePhoneNumberFragment);

    void inject(PhoneVerificationFragment phoneVerificationFragment);

    void inject(ProfileCompletionPhoneVerificationFragment profileCompletionPhoneVerificationFragment);


}
