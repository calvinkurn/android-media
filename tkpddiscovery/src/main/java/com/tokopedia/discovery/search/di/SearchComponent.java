package com.tokopedia.discovery.search.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.search.view.SearchFragment;

import dagger.Component;

/**
 * @author erry on 23/02/17.
 */

@SearchScope
@Component(modules = SearchModule.class, dependencies = AppComponent.class)
public interface SearchComponent {
    void inject(SearchFragment fragment);
}
