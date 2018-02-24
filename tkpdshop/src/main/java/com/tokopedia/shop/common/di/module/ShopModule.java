package com.tokopedia.shop.common.di.module;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonWS4Api;
import com.tokopedia.shop.common.di.ShopQualifier;
import com.tokopedia.shop.common.di.ShopWS4Qualifier;
import com.tokopedia.shop.common.di.scope.ShopScope;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Module
public class ShopModule {

    @ShopScope
    @Provides
    public CacheApiInterceptor provideApiCacheInterceptor() {
        return new CacheApiInterceptor();

    }

    @ShopQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(ShopAuthInterceptor shopAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }


    @ShopQualifier
    @ShopScope
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopScope
    @Provides
    public ShopCommonApi provideShopCommonApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonApi.class);
    }

    @ShopWS4Qualifier
    @ShopScope
    @Provides
    public Retrofit provideWS4Retrofit(@ShopQualifier OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_URL_WS).client(okHttpClient).build();
    }

    @ShopScope
    @Provides
    public ShopCommonWS4Api provideShopCommonWs4Api(@ShopWS4Qualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonWS4Api.class);
    }

    @ShopScope
    @Provides
    public ShopApi provideShopApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @ShopScope
    @Provides
    public ShopCommonCloudDataSource provideShopCommonCloudDataSource(ShopCommonApi shopCommonApi, ShopCommonWS4Api shopCommonWS4Api, UserSession userSession) {
        return new ShopCommonCloudDataSource(shopCommonApi, shopCommonWS4Api, userSession);
    }

    @ShopScope
    @Provides
    public ShopCommonDataSource provideShopCommonDataSource(ShopCommonCloudDataSource shopInfoCloudDataSource) {
        return new ShopCommonDataSource(shopInfoCloudDataSource);
    }

    @ShopScope
    @Provides
    public ShopCommonRepository provideShopCommonRepository(ShopCommonDataSource shopInfoDataSource) {
        return new ShopCommonRepositoryImpl(shopInfoDataSource);
    }

    @ShopScope
    @Provides
    public GetShopInfoUseCase provideGetShopInfoUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoUseCase(shopCommonRepository);
    }

    @ShopScope
    @Provides
    public GetShopInfoByDomainUseCase provideGetShopInfoByDomainUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoByDomainUseCase(shopCommonRepository);
    }

    @ShopScope
    @Provides
    public ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(ShopCommonRepository shopCommonRepository) {
        return new ToggleFavouriteShopUseCase(shopCommonRepository);
    }
}