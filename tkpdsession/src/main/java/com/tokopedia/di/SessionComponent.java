package com.tokopedia.di;

import com.tokopedia.core.base.di.component.AppComponent;

import dagger.Component;

/**
 * @author by nisie on 10/10/17.
 */

@SessionScope
@Component(modules = SessionModule.class, dependencies = AppComponent.class)
public interface SessionComponent {
//    void inject(LoginPhoneNumberFragment loginPhoneNumberFragment);

}
