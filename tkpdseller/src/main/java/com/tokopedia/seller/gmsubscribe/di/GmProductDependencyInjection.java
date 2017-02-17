package com.tokopedia.seller.gmsubscribe.di;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.seller.common.data.executor.JobExecutor;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.common.presentation.UIThread;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.product.GmSubscribeProductDataSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GmSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmSubscribeCurrentProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmSubscribeExtendProductUseCase;
import com.tokopedia.seller.gmsubscribe.view.product.presenter.GmProductPresenterImpl;
import com.tokopedia.seller.network.interceptor.GMSubscribeInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmProductDependencyInjection {

    public static GmProductPresenterImpl getPresenter() {
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GmSubscribeProductCache gmSubscribeProductCache = new GmSubscribeProductCache(globalCacheManager);
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
        Gson gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.GOLD_MERCHANT_DOMAIN)
                .client(client)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TkpdResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ;
        GmSubscribeProductCloud gmSubscribeProductCloud = new GmSubscribeProductCloud(retrofit);
        GmSubscribeProductMapper gmSubscribeProductMapper = new GmSubscribeProductMapper();
        GmSubscribeProductDataSource gmSubscribeProductDataSource = new GmSubscribeProductDataSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper, gson);
        GmSubscribeProductFactory gmSubscribeProductFactory = new GmSubscribeProductFactory(gmSubscribeProductDataSource);
        GmSubscribeProductRepositoryImpl gmSubscribeProductRepository = new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);
        ThreadExecutor threadExecutor = new JobExecutor();
        PostExecutionThread postExecutionThread = new UIThread();
        GetGmSubscribeCurrentProductUseCase getGmSubscribeCurrentProductUseCase = new GetGmSubscribeCurrentProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        GetGmSubscribeExtendProductUseCase getGmSubscribeExtendProductUseCase = new GetGmSubscribeExtendProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        return new GmProductPresenterImpl(getGmSubscribeCurrentProductUseCase, getGmSubscribeExtendProductUseCase);
    }
}
