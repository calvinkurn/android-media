package com.tokopedia.seller.topads.keyword.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.product.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.domain.ShopInfoRepository;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.keyword.data.repository.TopAdsKeywordRepositoryImpl;
import com.tokopedia.seller.topads.keyword.data.source.KeywordDashboardDataSouce;
import com.tokopedia.seller.topads.keyword.data.source.cloud.api.KeywordApi;
import com.tokopedia.seller.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.seller.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.seller.topads.keyword.domain.interactor.KeywordAddUseCase;
import com.tokopedia.seller.topads.keyword.domain.interactor.KeywordDashboardUseCase;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordAddPresenter;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordAddPresenterImpl;
import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

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
