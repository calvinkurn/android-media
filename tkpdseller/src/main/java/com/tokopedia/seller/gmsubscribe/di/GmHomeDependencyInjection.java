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
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.service.GmSubscribeProductService;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.ClearGmSubscribeProductCacheUseCase;
import com.tokopedia.seller.gmsubscribe.view.presenter.GmHomePresenterImpl;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBearerWithAuthInterceptor;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;

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

        GmSubscribeProductService gmSubscribeProductService = new GmSubscribeProductService();
        GmSubscribeProductCloud gmSubscribeProductCloud = new GmSubscribeProductCloud(gmSubscribeProductService.getApi());

        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GmSubscribeProductCache gmSubscribeProductCache = new GmSubscribeProductCache(globalCacheManager);

        GmSubscribeProductMapper gmSubscribeProductMapper = new GmSubscribeProductMapper();
        GmSubscribeProductDataSource gmSubscribeProductDataSource = new GmSubscribeProductDataSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper);
        GmSubscribeProductFactory gmSubscribeProductFactory = new GmSubscribeProductFactory(gmSubscribeProductDataSource);
        GmSubscribeProductRepositoryImpl gmSubscribeProductReposistory = new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);

        ClearGmSubscribeProductCacheUseCase clearGMSubscribeProductCache = new ClearGmSubscribeProductCacheUseCase(threadExecutor, postExecutionThread, gmSubscribeProductReposistory);

        return new GmHomePresenterImpl(clearGMSubscribeProductCache);
    }
}
