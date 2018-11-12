package com.tokopedia.discovery.newdiscovery.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.autocomplete.di.AutoCompleteModule;
import com.tokopedia.discovery.newdiscovery.di.module.net.DiscoveryInterceptorModule;
import com.tokopedia.discovery.newdiscovery.di.module.net.DiscoveryNetModule;
import com.tokopedia.discovery.newdiscovery.di.module.net.DiscoveryOkHttpClientModule;
import com.tokopedia.discovery.newdiscovery.di.scope.DiscoveryScope;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;

import dagger.Component;

@DiscoveryScope
@Component(modules = {
            DiscoveryNetModule.class,
            DiscoveryOkHttpClientModule.class,
            DiscoveryInterceptorModule.class,
            AutoCompleteModule.class
        },dependencies = BaseAppComponent.class)
public interface DiscoveryComponent {
    @ApplicationContext Context context();

    void inject(SearchMainFragment fragment);

    void inject(SearchPresenter presenter);
}
