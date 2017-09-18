package com.tokopedia.topads.keyword.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsModule;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordListFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNegativeListFragment;

import dagger.Component;

@TopAdsKeywordScope
@Component(modules = TopAdsModule.class, dependencies = AppComponent.class)
public interface TopAdsKeywordComponent {
    void inject(TopAdsKeywordListFragment topAdsKeywordListFragment);

    void inject(TopAdsKeywordNegativeListFragment topAdsKeywordNegativeListFragment);
}
