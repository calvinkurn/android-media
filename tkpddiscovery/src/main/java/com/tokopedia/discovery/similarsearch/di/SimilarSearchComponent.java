package com.tokopedia.discovery.similarsearch.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.similarsearch.di.scope.SimilarSearchModuleScope;
import com.tokopedia.discovery.similarsearch.view.SimilarSearchFragment;

import dagger.Component;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@SimilarSearchModuleScope
@Component(modules = {SimilarSearchModule.class, }, dependencies = {AppComponent.class})
public interface SimilarSearchComponent {
    void inject(SimilarSearchFragment view);
}