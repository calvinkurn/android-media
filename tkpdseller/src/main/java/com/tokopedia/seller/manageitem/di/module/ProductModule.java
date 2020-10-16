package com.tokopedia.seller.manageitem.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.seller.manageitem.data.cloud.api.ShopApi;
import com.tokopedia.seller.manageitem.data.cloud.api.TomeProductApi;
import com.tokopedia.seller.manageitem.data.source.ShopInfoDataSource;
import com.tokopedia.seller.manageitem.di.scope.ProductScope;
import com.tokopedia.seller.manageitem.domain.repository.ShopInfoRepository;
import com.tokopedia.seller.manageitem.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductScope
@Module
public class ProductModule {


    @ProductScope
    @Provides
    CacheManager provideCacheManager(){
        return PersistentCacheManager.instance;
    }

    @ProductScope
    @Provides
    public ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @ProductScope
    @Provides
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @ProductScope
    @Provides
    TomeProductApi provideTomeApi(@ProductTomeQualifier Retrofit retrofit){
        return retrofit.create(TomeProductApi.class);
    }

    @ProductTomeQualifier
    @ProductScope
    @Provides
    Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder, @ProductTomeQualifier OkHttpClient okHttpClient){
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getTOME()).client(okHttpClient).build();
    }

    @ProductTomeQualifier
    @ProductScope
    @Provides
    public OkHttpClient provideOkHttpClientTomeBearerAuth(@ProductTomeQualifier HttpLoggingInterceptor httpLoggingInterceptor,
                                                          BearerInterceptor bearerInterceptor,
                                                          @ProductTomeQualifier ErrorResponseInterceptor errorResponseInterceptor
    ) {
        return new OkHttpClient.Builder()
                .addInterceptor(bearerInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ProductTomeQualifier
    @ProductScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @ProductTomeQualifier
    @ProductScope
    @Provides
    public ErrorResponseInterceptor provideResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class);
    }

    @ProductScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}