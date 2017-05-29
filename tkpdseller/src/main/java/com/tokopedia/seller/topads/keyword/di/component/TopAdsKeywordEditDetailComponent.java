package com.tokopedia.seller.topads.keyword.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordEditDetailModule;
import com.tokopedia.seller.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordEditDetailFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 5/26/17.
 */
@TopAdsKeywordScope
@Component(modules = TopAdsKeywordEditDetailModule.class, dependencies = AppComponent.class)
public interface TopAdsKeywordEditDetailComponent {

    void inject(TopAdsKeywordEditDetailFragment topAdsKeywordEditDetailFragment);
}
