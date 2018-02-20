package com.tokopedia.shop.product.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor;
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl;
import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.shop.product.data.repository.ShopProductRepositoryImpl;
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource;
import com.tokopedia.shop.product.di.ShopProductGMFeaturedQualifier;
import com.tokopedia.shop.product.di.scope.ShopProductScope;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@ShopProductScope
@Module
public class ShopProductModule {

    @Provides
    public GMAuthInterceptor provideGMAuthInterceptor(@ApplicationContext Context context,
                                             AbstractionRouter abstractionRouter,
                                             UserSession userSession) {
        return new GMAuthInterceptor(context, abstractionRouter, userSession);
    }

    @ShopProductGMFeaturedQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(GMAuthInterceptor gmAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(gmAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopProductGMFeaturedQualifier
    @ShopProductScope
    @Provides
    public Retrofit provideRetrofit(@ShopProductGMFeaturedQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopProductScope
    @Provides
    public GMCommonApi provideGMCommonApi(@ShopProductGMFeaturedQualifier Retrofit retrofit) {
        return retrofit.create(GMCommonApi.class);
    }

    @ShopProductScope
    @Provides
    public GMCommonCloudDataSource provideGMCommonCloudDataSource(GMCommonApi gmCommonApi) {
        return new GMCommonCloudDataSource(gmCommonApi);
    }

    @ShopProductScope
    @Provides
    public GMCommonDataSource provideGMCommonDataSource(GMCommonCloudDataSource gmCommonCloudDataSource) {
        return new GMCommonDataSource(gmCommonCloudDataSource);
    }

    @ShopProductScope
    @Provides
    public GMCommonRepository provideGMCommonRepository(GMCommonDataSource gmCommonDataSource) {
        return new GMCommonRepositoryImpl(gmCommonDataSource);
    }

    @ShopProductScope
    @Provides
    public GetFeatureProductListUseCase provideGetFeatureProductListUseCase(GMCommonRepository gmCommonRepository) {
        return new GetFeatureProductListUseCase(gmCommonRepository);
    }

    @ShopProductScope
    @Provides
    public ShopProductRepository provideShopProductRepository(ShopProductCloudDataSource shopProductDataSource) {
        return new ShopProductRepositoryImpl(shopProductDataSource);
    }

    @ShopProductScope
    @Provides
    public ShopProductViewModel provideShopProductViewModel() {
        return new ShopProductViewModel();
    }
}