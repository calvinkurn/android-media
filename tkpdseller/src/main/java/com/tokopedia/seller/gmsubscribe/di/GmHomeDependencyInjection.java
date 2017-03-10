package com.tokopedia.seller.gmsubscribe.di;

import com.google.gson.Gson;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.product.GmSubscribeProductDataSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GmSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.api.GoldMerchantApi;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.ClearGmSubscribeProductCacheUseCase;
import com.tokopedia.seller.gmsubscribe.view.presenter.GmHomePresenterImpl;
import com.tokopedia.seller.network.interceptor.GMSubscribeInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sebastianuskh on 2/9/17.
 */
public class GmHomeDependencyInjection {
    public static GmHomePresenterImpl getPresenter() {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();
        Gson gson = new Gson();

        GeneratedHostConverter hostConverter = new GeneratedHostConverter();
        TkpdResponseConverter tkpdResponseConverter = new TkpdResponseConverter();
        StringResponseConverter stringResponseConverter = new StringResponseConverter();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        RxJavaCallAdapterFactory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.readTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(45L, TimeUnit.SECONDS);
        GMSubscribeInterceptor authInterceptor = new GMSubscribeInterceptor();
        clientBuilder.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.interceptors().add(logInterceptor);
        OkHttpClient client = clientBuilder.build();

        Retrofit retrofit = createRetrofit(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                client,
                hostConverter,
                tkpdResponseConverter,
                stringResponseConverter,
                gsonConverterFactory,
                rxJavaCallAdapterFactory);
        GmSubscribeProductCloud gmSubscribeProductCloud = new GmSubscribeProductCloud(retrofit.create(GoldMerchantApi.class));

        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GmSubscribeProductCache gmSubscribeProductCache = new GmSubscribeProductCache(globalCacheManager);

        GmSubscribeProductMapper gmSubscribeProductMapper = new GmSubscribeProductMapper();
        GmSubscribeProductDataSource gmSubscribeProductDataSource = new GmSubscribeProductDataSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper);
        GmSubscribeProductFactory gmSubscribeProductFactory = new GmSubscribeProductFactory(gmSubscribeProductDataSource);
        GmSubscribeProductRepositoryImpl gmSubscribeProductReposistory = new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);

        ClearGmSubscribeProductCacheUseCase clearGMSubscribeProductCache = new ClearGmSubscribeProductCacheUseCase(threadExecutor, postExecutionThread, gmSubscribeProductReposistory);

        return new GmHomePresenterImpl(clearGMSubscribeProductCache);
    }

    private static Retrofit createRetrofit(String baseUrl,
                                           OkHttpClient client,
                                           GeneratedHostConverter hostConverter,
                                           TkpdResponseConverter tkpdResponseConverter,
                                           StringResponseConverter stringResponseConverter,
                                           GsonConverterFactory gsonConverterFactory,
                                           RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(hostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }
}
