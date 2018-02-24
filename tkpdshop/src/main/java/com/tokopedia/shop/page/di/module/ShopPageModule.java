package com.tokopedia.shop.page.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.reputation.common.data.interceptor.ReputationAuthInterceptor;
import com.tokopedia.reputation.common.data.repository.ReputationCommonRepositoryImpl;
import com.tokopedia.reputation.common.data.source.ReputationCommonDataSource;
import com.tokopedia.reputation.common.data.source.cloud.ReputationCommonCloudDataSource;
import com.tokopedia.reputation.common.data.source.cloud.api.ReputationCommonApi;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase;
import com.tokopedia.reputation.common.domain.repository.ReputationCommonRepository;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.page.di.ShopInfoReputationSpeedQualifier;
import com.tokopedia.shop.page.di.scope.ShopPageScope;
import com.tokopedia.shop.page.domain.interactor.GetShopPageDataByDomainUseCase;
import com.tokopedia.shop.page.domain.interactor.GetShopPageDataUseCase;
import com.tokopedia.shop.page.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@ShopPageScope
@Module
public class ShopPageModule {

    @Provides
    public ReputationAuthInterceptor provideReputationAuthInterceptor(@ApplicationContext Context context,
                                                                      AbstractionRouter abstractionRouter,
                                                                      UserSession userSession) {
        return new ReputationAuthInterceptor(context, abstractionRouter, userSession);
    }

    @ShopInfoReputationSpeedQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(ReputationAuthInterceptor reputationAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(reputationAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopInfoReputationSpeedQualifier
    @ShopPageScope
    @Provides
    public Retrofit provideRetrofit(@ShopInfoReputationSpeedQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ReputationCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopPageScope
    @Provides
    public ReputationCommonApi provideReputationCommonApi(@ShopInfoReputationSpeedQualifier Retrofit retrofit) {
        return retrofit.create(ReputationCommonApi.class);
    }

    @ShopPageScope
    @Provides
    public ReputationCommonCloudDataSource provideReputationCommonCloudDataSource(ReputationCommonApi reputationCommonApi) {
        return new ReputationCommonCloudDataSource(reputationCommonApi);
    }

    @ShopPageScope
    @Provides
    public ReputationCommonDataSource provideReputationCommonDataSource(ReputationCommonCloudDataSource reputationCommonCloudDataSource) {
        return new ReputationCommonDataSource(reputationCommonCloudDataSource);
    }

    @ShopPageScope
    @Provides
    public ReputationCommonRepository provideReputationCommonRepository(ReputationCommonDataSource reputationCommonDataSource) {
        return new ReputationCommonRepositoryImpl(reputationCommonDataSource);
    }

    @ShopPageScope
    @Provides
    public GetReputationSpeedUseCase provideGetFeatureProductListUseCase(ReputationCommonRepository reputationCommonRepository) {
        return new GetReputationSpeedUseCase(reputationCommonRepository);
    }

    @ShopPageScope
    @Provides
    public DeleteShopInfoUseCase provideDeleteShopInfoUseCase() {
        return new DeleteShopInfoUseCase();
    }

    @ShopPageScope
    @Provides
    public GetShopPageDataUseCase provideGetShopPageDataUseCase(
            GetShopInfoUseCase getShopInfoUseCase,
            GetReputationSpeedUseCase deleteShopInfoUseCase) {
        return new GetShopPageDataUseCase(getShopInfoUseCase, deleteShopInfoUseCase);
    }

    @ShopPageScope
    @Provides
    public GetShopPageDataByDomainUseCase provideGetShopPageDataByDomainUseCase(
            GetShopInfoByDomainUseCase getShopInfoByDomainUseCase,
            GetReputationSpeedUseCase deleteShopInfoUseCase) {
        return new GetShopPageDataByDomainUseCase(getShopInfoByDomainUseCase, deleteShopInfoUseCase);
    }

    @ShopPageScope
    @Provides
    public ToggleFavouriteShopAndDeleteCacheUseCase provideToggleFavouriteShopAndDeleteCacheUseCase(
            ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
            DeleteShopInfoUseCase deleteShopInfoUseCase) {
        return new ToggleFavouriteShopAndDeleteCacheUseCase(toggleFavouriteShopUseCase, deleteShopInfoUseCase);
    }
}