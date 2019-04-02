package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
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

    void inject(AddNameRegisterPhoneFragment addNameFragment);

    GetUserInfoUseCase getUserInfoUseCase();

    UserSession userSession();
}
