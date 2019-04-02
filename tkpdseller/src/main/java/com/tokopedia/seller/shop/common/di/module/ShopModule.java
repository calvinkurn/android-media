package com.tokopedia.seller.shop.common.di.module;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.product.manage.item.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.product.manage.item.common.data.source.ShopInfoDataSource;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.common.exception.model.TomeErrorResponse;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.common.di.ShopScope;
import com.tokopedia.seller.shop.common.interceptor.HeaderErrorResponseInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 10/20/17.
 */

@ShopScope
@Module
public class ShopModule {

    @ShopScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @ShopScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @ShopScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideShopModelMapper() {
        return new SimpleDataResponseMapper<>();
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.TOME_DOMAIN).client(okHttpClient).build();
    }

    @ShopScope
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

    @ShopQualifier
    @ShopScope
    @Provides
    public OkHttpClient provideOkHttpClientTomeBearerAuth(HttpLoggingInterceptor httpLoggingInterceptor,
                                                          BearerInterceptor bearerInterceptor,
                                                          @ShopQualifier ErrorResponseInterceptor errorResponseInterceptor,
                                                          FingerprintInterceptor fingerprintInterceptor,
                                                          ChuckInterceptor chuckInterceptor
    ) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(bearerInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(fingerprintInterceptor);
        if (GlobalConfig.DEBUG) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(TomeErrorResponse.class);
    }

    @ShopScope
    @Provides
    public GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @ShopScope
    @Provides
    public GetShopInfoUseCase provideGetShopInfoUseCase(ShopInfoRepository shopInfoRepository,
                                                        ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread) {
        return new GetShopInfoUseCase(threadExecutor, postExecutionThread, shopInfoRepository);
    }
}

