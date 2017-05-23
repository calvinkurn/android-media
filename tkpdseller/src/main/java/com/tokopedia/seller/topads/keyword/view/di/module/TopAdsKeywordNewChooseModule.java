package com.tokopedia.seller.topads.keyword.view.di.module;

import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.seller.product.domain.ShopInfoRepository;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.keyword.view.data.repository.TopAdsKeywordRepositoryImpl;
import com.tokopedia.seller.topads.keyword.view.data.source.KeywordDashboardDataSouce;
import com.tokopedia.seller.topads.keyword.view.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.view.domain.TopAdsKeywordRepository;
import com.tokopedia.seller.topads.keyword.view.domain.interactor.KeywordDashboardUseCase;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hendry on 5/18/2017.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordNewChooseModule {

    @TopAdsKeywordScope
    @Provides
    TopAdsManagementApi provideKeywordApi(@TopAdsQualifier Retrofit retrofit){
        return retrofit.create(TopAdsManagementApi.class);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordRepository provideTopAdsKeywordRepository
            (KeywordDashboardDataSouce keywordDashboardDataSouce,
             ShopInfoRepository shopInfoRepository) {
        return new TopAdsKeywordRepositoryImpl(keywordDashboardDataSouce, shopInfoRepository);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordListPresenterImpl provideTopAdsKeywordListPresenter(
            KeywordDashboardUseCase keywordDashboardUseCase
    ) {
        return new TopAdsKeywordListPresenterImpl(keywordDashboardUseCase);
    }

}
