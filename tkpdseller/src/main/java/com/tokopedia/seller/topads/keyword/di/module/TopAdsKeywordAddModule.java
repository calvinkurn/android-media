package com.tokopedia.seller.topads.keyword.di.module;

import com.tokopedia.seller.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.domain.interactor.KeywordAddUseCase;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordAddPresenter;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordAddPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hendry on 5/18/2017.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordAddModule extends TopAdsModule{

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordAddPresenter provideTopAdsKeywordAddPresenter(
            KeywordAddUseCase keywordAddUseCase) {
        return new TopAdsKeywordAddPresenterImpl(keywordAddUseCase);
    }

}
