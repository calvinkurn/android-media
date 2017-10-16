package com.tokopedia.session.di;

import com.tokopedia.core.base.di.component.AppComponent;
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


}
