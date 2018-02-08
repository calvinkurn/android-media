package com.tokopedia.profilecompletion.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionPhoneVerificationFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 7/10/17.
 */


@PhoneVerifScope
@Component(modules = PhoneVerifModule.class, dependencies = AppComponent.class)
public interface PhoneVerifComponent {
    void inject(ProfileCompletionPhoneVerificationFragment fragment);
}
