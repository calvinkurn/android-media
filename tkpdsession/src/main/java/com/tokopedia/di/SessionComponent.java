package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberEmailFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberEmailVerificationFragment;
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

    void inject(VerificationActivity loginFragment);

    void inject(VerificationFragment verificationFragment);

    void inject(ChooseVerificationMethodFragment chooseVerificationMethodFragment);

    void inject(ChangePhoneNumberWarningFragment fragment);

    void inject(ChangePhoneNumberEmailFragment fragment);

    void inject(ChangePhoneNumberEmailVerificationFragment fragment);
}
