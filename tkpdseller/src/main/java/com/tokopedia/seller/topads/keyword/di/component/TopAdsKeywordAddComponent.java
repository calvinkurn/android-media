package com.tokopedia.seller.topads.keyword.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordAddModule;
import com.tokopedia.seller.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordAddFragment;

import dagger.Component;

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordAddModule.class, dependencies = AppComponent.class)
public interface TopAdsKeywordAddComponent {
    void inject(TopAdsKeywordAddFragment topAdsKeywordAddFragment);
}
