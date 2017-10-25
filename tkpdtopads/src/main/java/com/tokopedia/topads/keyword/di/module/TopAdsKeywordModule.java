package com.tokopedia.topads.keyword.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.edit.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;
import com.tokopedia.topads.dashboard.di.scope.TopAdsQualifier;
import com.tokopedia.topads.keyword.data.repository.TopAdsKeywordRepositoryImpl;
import com.tokopedia.topads.keyword.data.source.KeywordDashboardDataSouce;
import com.tokopedia.topads.keyword.data.source.cloud.api.KeywordApi;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.interactor.KeywordDashboardUseCase;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hendry on 5/18/2017.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordModule {

    @TopAdsKeywordScope
    @Provides
    KeywordApi provideKeywordApi(@TopAdsQualifier Retrofit retrofit){
        return retrofit.create(KeywordApi.class);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordRepository provideTopAdsKeywordRepository(
            KeywordDashboardDataSouce keywordDashboardDataSouce,
            ShopInfoRepository shopInfoRepository) {
        return new TopAdsKeywordRepositoryImpl(
                keywordDashboardDataSouce, shopInfoRepository);
    }

    @TopAdsKeywordScope
    @Provides
    TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit) {
        return retrofit.create(TomeApi.class);
    }

    @TopAdsKeywordScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideShopModelMapper() {
        return new SimpleDataResponseMapper<>();
    }

}
