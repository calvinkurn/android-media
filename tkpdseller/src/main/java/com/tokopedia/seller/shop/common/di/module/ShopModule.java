package com.tokopedia.seller.shop.common.di.module;

import android.content.Context;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.seller.shop.common.exception.model.ShopErrorResponse;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.common.di.ShopScope;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.shop.common.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.seller.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.shop.common.data.source.ShopInfoDataSource;
import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;

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
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @ShopScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @ShopScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideShopModelMapper(){
        return new SimpleDataResponseMapper<>();
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public TomeApi provideTomeApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(TomeApi.class);
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder){
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
                                                          @ShopQualifier TkpdErrorResponseInterceptor tkpdErrorResponseInterceptor
                                                          ) {
        return new OkHttpClient.Builder()
                .addInterceptor(bearerInterceptor)
                .addInterceptor(tkpdErrorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopQualifier
    @ShopScope
    @Provides
    public TkpdErrorResponseInterceptor provideTkpdErrorResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(ShopErrorResponse.class);
    }

    @ShopScope
    @Provides
    public ShopOpenTracking provideTrackingOpenShop(@ApplicationContext Context context){
        if(context instanceof SellerModuleRouter) {
            return new ShopOpenTracking((SellerModuleRouter)context);
        }else{
            return null;
        }
    }

    @ShopScope
    @Provides
    public GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }

    @ShopScope
    @Provides
    public GetShopInfoUseCase provideGetShopInfoUseCase(ShopInfoRepository shopInfoRepository,
                                                        ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread){
        return new GetShopInfoUseCase(threadExecutor, postExecutionThread, shopInfoRepository);
    }
}

