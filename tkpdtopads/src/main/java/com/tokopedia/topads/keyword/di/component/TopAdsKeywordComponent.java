package com.tokopedia.topads.keyword.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordModule;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordListFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNegativeListFragment;

import dagger.Component;

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordModule.class, dependencies = TopAdsComponent.class)
public interface TopAdsKeywordComponent {
    void inject(TopAdsKeywordListFragment topAdsKeywordListFragment);

    void inject(TopAdsKeywordNegativeListFragment topAdsKeywordNegativeListFragment);
}
