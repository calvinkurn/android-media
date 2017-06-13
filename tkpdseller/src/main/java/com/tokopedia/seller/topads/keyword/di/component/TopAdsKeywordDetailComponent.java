package com.tokopedia.seller.topads.keyword.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordDetailModule;
import com.tokopedia.seller.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordDetailFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordDetailModule.class, dependencies = AppComponent.class)
public interface TopAdsKeywordDetailComponent {
    void inject(TopAdsKeywordDetailFragment topAdsKeywordDetailFragment);
}
