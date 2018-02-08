package com.tokopedia.discovery.newdiscovery.hotlist.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity;
import com.tokopedia.discovery.newdiscovery.hotlist.di.module.HotlistModule;
import com.tokopedia.discovery.newdiscovery.hotlist.di.scope.HotlistScope;
import com.tokopedia.discovery.newdiscovery.hotlist.view.fragment.HotlistFragment;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentPresenter;

import dagger.Component;

/**
 * Created by hangnadi on 10/5/17.
 */

@HotlistScope
@Component(modules = HotlistModule.class, dependencies = AppComponent.class)
public interface HotlistComponent {

    void inject(HotlistActivity activity);

    void inject(HotlistFragment fragment);

    void inject(HotlistFragmentPresenter presenter);

}
