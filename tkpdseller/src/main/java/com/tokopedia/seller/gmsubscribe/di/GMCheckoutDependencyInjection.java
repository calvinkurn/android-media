package com.tokopedia.seller.gmsubscribe.di;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.seller.common.data.executor.JobExecutor;
import com.tokopedia.seller.common.presentation.UIThread;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeCartFactory;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.mapper.GmSubscribeCheckoutMapper;
import com.tokopedia.seller.gmsubscribe.data.mapper.cart.GmSubscribeVoucherMapper;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GmSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeCartRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GmSubscribeCartCloud;
import com.tokopedia.seller.gmsubscribe.data.source.product.GmSubscribeProductDataSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GmSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GMSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckGmSubscribeVoucherUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGmSubscribeUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGmSubscribeWithVoucherCheckUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmAutoSubscribeSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmCurrentSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.view.checkout.presenter.GmCheckoutPresenterImpl;
import com.tokopedia.seller.network.interceptor.GMSubscribeInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmCheckoutDependencyInjection {
    public static GmCheckoutPresenterImpl createPresenter() {
        Gson gson = new Gson();

        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();


        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GmSubscribeProductCache gmSubscribeProductCache = new GmSubscribeProductCache(globalCacheManager);

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

        Retrofit productRetrofit = createRetrofit(TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                client,
                hostConverter,
                tkpdResponseConverter,
                stringResponseConverter,
                gsonConverterFactory,
                rxJavaCallAdapterFactory);
        Retrofit cartRetrofit = createRetrofit(TkpdBaseURL.TOKOPEDIA_CART_DOMAIN,
                client,
                hostConverter,
                tkpdResponseConverter,
                stringResponseConverter,
                gsonConverterFactory,
                rxJavaCallAdapterFactory);

        GMSubscribeProductCloud gmSubscribeProductCloud = new GMSubscribeProductCloud(productRetrofit);
        GmSubscribeCartCloud cartCloud = new GmSubscribeCartCloud(cartRetrofit);

        GmSubscribeProductMapper gmSubscribeProductMapper = new GmSubscribeProductMapper();
        GmSubscribeVoucherMapper voucherMapper = new GmSubscribeVoucherMapper();
        GmSubscribeCheckoutMapper checkoutMapper = new GmSubscribeCheckoutMapper();

        GmSubscribeProductDataSource gmSubscribeProductDataSource = new GmSubscribeProductDataSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper, gson);
        GmSubscribeProductFactory gmSubscribeProductFactory = new GmSubscribeProductFactory(gmSubscribeProductDataSource);
        GmSubscribeCartFactory gmSubscribeCartFactory = new GmSubscribeCartFactory(cartCloud, gson, voucherMapper, checkoutMapper);
        GmSubscribeCartRepositoryImpl gmSubscribeCartRepository = new GmSubscribeCartRepositoryImpl(gmSubscribeCartFactory);
        GmSubscribeProductRepositoryImpl gmSubscribeProductRepository = new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);

        GetGmCurrentSelectedProductUseCase getCurrentSelectedProduct = new GetGmCurrentSelectedProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        GetGmAutoSubscribeSelectedProductUseCase getGmAutoSubscribeSelectedProductUseCase = new GetGmAutoSubscribeSelectedProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        CheckGmSubscribeVoucherUseCase checkGmSubscribeVoucherUseCase = new CheckGmSubscribeVoucherUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);
        CheckoutGmSubscribeUseCase checkoutGmSubscribeUseCase = new CheckoutGmSubscribeUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);
        CheckoutGmSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase = new CheckoutGmSubscribeWithVoucherCheckUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);

        return new GmCheckoutPresenterImpl(getCurrentSelectedProduct, getGmAutoSubscribeSelectedProductUseCase, checkGmSubscribeVoucherUseCase, checkoutGmSubscribeUseCase, checkoutGMSubscribeWithVoucherCheckUseCase);
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
