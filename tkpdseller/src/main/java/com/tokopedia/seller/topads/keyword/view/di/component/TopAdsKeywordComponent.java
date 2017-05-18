package com.tokopedia.seller.topads.keyword.view.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.keyword.view.di.module.TopAdsModule;
import com.tokopedia.seller.topads.keyword.view.di.scope.TopAdsKeywordScope;

import dagger.Component;

@TopAdsKeywordScope
@Component(modules = TopAdsModule.class, dependencies = AppComponent.class)
public interface TopAdsKeywordComponent {

}
