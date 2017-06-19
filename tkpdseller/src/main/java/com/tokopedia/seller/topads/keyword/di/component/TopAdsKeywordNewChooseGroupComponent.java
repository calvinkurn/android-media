package com.tokopedia.seller.topads.keyword.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;
import com.tokopedia.seller.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordGroupsFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordNewChooseGroupFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 5/24/17.
 */

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordNewChooseGroupModule.class, dependencies = AppComponent.class)
public interface TopAdsKeywordNewChooseGroupComponent {
    void inject(TopAdsKeywordNewChooseGroupFragment topAdsKeywordNewChooseGroupFragment);

    void inject(TopAdsKeywordGroupsFragment topAdsKeywordGroupsFragment);
}
