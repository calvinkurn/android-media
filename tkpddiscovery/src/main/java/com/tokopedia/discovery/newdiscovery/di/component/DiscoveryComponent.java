package com.tokopedia.discovery.newdiscovery.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryCatalogFragment;
import com.tokopedia.discovery.newdiscovery.di.module.DiscoveryModule;
import com.tokopedia.discovery.newdiscovery.di.scope.DiscoveryScope;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogPresenter;

import dagger.Component;

/**
 * Created by henrypriyono on 10/10/17.
 */

@DiscoveryScope
@Component(modules = DiscoveryModule.class, dependencies = AppComponent.class)
public interface DiscoveryComponent {

    void inject(CategoryCatalogFragment catalogFragment);

    void inject(CatalogPresenter presenter);
}
