package com.tokopedia.seller.shopscore.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shopscore.data.factory.ShopScoreFactory;
import com.tokopedia.seller.shopscore.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.seller.shopscore.data.repository.ShopScoreRepositoryImpl;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.data.source.cloud.api.ShopScoreApi;
import com.tokopedia.seller.shopscore.data.source.disk.ShopScoreCache;
import com.tokopedia.seller.shopscore.domain.interactor.GetShopScoreDetailUseCase;
import com.tokopedia.seller.shopscore.view.presenter.ShopScoreDetailPresenterImpl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailDependencyInjector {
    public static ShopScoreDetailPresenterImpl getPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();
        Gson gson = new Gson();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.readTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(45L, TimeUnit.SECONDS);
        TkpdAuthInterceptor authInterceptor = new TkpdAuthInterceptor();
        clientBuilder.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.interceptors().add(logInterceptor);
        OkHttpClient client = clientBuilder.build();

        Retrofit retrofit = createRetrofit(
                TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                client,
                new GeneratedHostConverter(),
                new TkpdResponseConverter(),
                new StringResponseConverter(),
                GsonConverterFactory.create(gson),
                RxJavaCallAdapterFactory.create()
        );

        ShopScoreApi api = retrofit.create(ShopScoreApi.class);
        SessionHandler sessionHandler = new SessionHandler(context);
        ShopScoreCloud shopScoreCloud = new ShopScoreCloud(api, sessionHandler);
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        ShopScoreCache shopScoreCache = new ShopScoreCache(globalCacheManager);
        ShopScoreDetailMapper shopScoreDetailMapper = new ShopScoreDetailMapper(context);
        ShopScoreFactory shopScoreFactory = new ShopScoreFactory(shopScoreCloud, shopScoreCache, shopScoreDetailMapper);
        ShopScoreRepositoryImpl shopScoreRepository = new ShopScoreRepositoryImpl(shopScoreFactory);
        GetShopScoreDetailUseCase getShopScoreDetailUseCase = new GetShopScoreDetailUseCase(threadExecutor, postExecutionThread, shopScoreRepository);
        return new ShopScoreDetailPresenterImpl(getShopScoreDetailUseCase);
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
