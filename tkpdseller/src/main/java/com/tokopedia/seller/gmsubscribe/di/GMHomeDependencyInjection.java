package com.tokopedia.seller.gmsubscribe.di;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.seller.common.data.executor.JobExecutor;
import com.tokopedia.seller.common.presentation.UIThread;
import com.tokopedia.seller.gmsubscribe.data.factory.GMSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GMSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.repository.GMSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.product.GMSubscribeProductListSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GMSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GMSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.ClearGMSubscribeProductCacheUseCase;
import com.tokopedia.seller.gmsubscribe.view.home.presenter.GMHomePresenterImpl;
import com.tokopedia.seller.network.interceptor.SessionAuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sebastianuskh on 2/9/17.
 */
public class GMHomeDependencyInjection {
    public static GMHomePresenterImpl getPresenter() {
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
        SessionAuthInterceptor authInterceptor = new SessionAuthInterceptor();
        clientBuilder.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.interceptors().add(logInterceptor);
        OkHttpClient client =  clientBuilder.build();

        Retrofit retrofit = createRetrofit(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                client,
                hostConverter,
                tkpdResponseConverter,
                stringResponseConverter,
                gsonConverterFactory,
                rxJavaCallAdapterFactory);
        GMSubscribeProductCloud gmSubscribeProductCloud = new GMSubscribeProductCloud(retrofit);

        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GMSubscribeProductCache gmSubscribeProductCache = new GMSubscribeProductCache(globalCacheManager);

        GMSubscribeProductMapper gmSubscribeProductMapper = new GMSubscribeProductMapper();
        GMSubscribeProductListSource gmSubscribeProductListSource = new GMSubscribeProductListSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper, gson);
        GMSubscribeProductFactory gmSubscribeProductFactory = new GMSubscribeProductFactory(gmSubscribeProductListSource);
        GMSubscribeProductRepositoryImpl gmSubscribeProductReposistory = new GMSubscribeProductRepositoryImpl(gmSubscribeProductFactory);

        ClearGMSubscribeProductCacheUseCase clearGMSubscribeProductCache = new ClearGMSubscribeProductCacheUseCase(threadExecutor, postExecutionThread, gmSubscribeProductReposistory);

        return new GMHomePresenterImpl(clearGMSubscribeProductCache);
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
