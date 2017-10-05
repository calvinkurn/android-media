package com.tokopedia.session.login.view.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.login.view.ReloginActivity;

import dagger.Component;

/**
 * @author by nisie on 5/26/17.
 */


@ReloginScope
@Component(modules = ReloginModule.class, dependencies = AppComponent.class)
public interface ReloginComponent {
    void inject(ReloginActivity reloginActivity);
}
