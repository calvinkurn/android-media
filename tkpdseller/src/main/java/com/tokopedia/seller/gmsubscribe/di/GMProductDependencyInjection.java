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
import com.tokopedia.seller.gmsubscribe.data.factory.GMSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GMSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.repository.GMSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.product.GMSubscribeProductListSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GMSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GMSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGMSubscribeCurrentProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGMSubscribeExtendProductUseCase;
import com.tokopedia.seller.gmsubscribe.view.product.presenter.GMProductPresenterImpl;
import com.tokopedia.seller.network.interceptor.GMSubscribeCartInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GMProductDependencyInjection {

    public static GMProductPresenterImpl getPresenter() {
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GMSubscribeProductCache gmSubscribeProductCache = new GMSubscribeProductCache(globalCacheManager);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.readTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(45L, TimeUnit.SECONDS);
        GMSubscribeCartInterceptor authInterceptor = new GMSubscribeCartInterceptor();
        clientBuilder.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.interceptors().add(logInterceptor);
        OkHttpClient client =  clientBuilder.build();
        Gson gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.GOLD_MERCHANT_DOMAIN)
                .client(client)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TkpdResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();;
        GMSubscribeProductCloud gmSubscribeProductCloud = new GMSubscribeProductCloud(retrofit);
        GMSubscribeProductMapper gmSubscribeProductMapper = new GMSubscribeProductMapper();
        GMSubscribeProductListSource gmSubscribeProductListSource = new GMSubscribeProductListSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper, gson);
        GMSubscribeProductFactory gmSubscribeProductFactory = new GMSubscribeProductFactory(gmSubscribeProductListSource);
        GMSubscribeProductRepositoryImpl gmSubscribeProductRepository = new GMSubscribeProductRepositoryImpl(gmSubscribeProductFactory);
        ThreadExecutor threadExecutor = new JobExecutor();
        PostExecutionThread postExecutionThread = new UIThread();
        GetGMSubscribeCurrentProductUseCase getGMSubscribeCurrentProductUseCase = new GetGMSubscribeCurrentProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        GetGMSubscribeExtendProductUseCase getGMSubscribeExtendProductUseCase = new GetGMSubscribeExtendProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        return new GMProductPresenterImpl(getGMSubscribeCurrentProductUseCase, getGMSubscribeExtendProductUseCase);
    }
}
