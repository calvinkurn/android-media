package com.tokopedia.seller.gmsubscribe.di;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.seller.common.data.executor.JobExecutor;
import com.tokopedia.seller.common.presentation.UIThread;
import com.tokopedia.seller.gmsubscribe.data.factory.GMSubscribeCartFactory;
import com.tokopedia.seller.gmsubscribe.data.factory.GMSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.mapper.GMSubscribeCheckoutMapper;
import com.tokopedia.seller.gmsubscribe.data.mapper.cart.GMSubscribeVoucherMapper;
import com.tokopedia.seller.gmsubscribe.data.mapper.product.GMSubscribeProductMapper;
import com.tokopedia.seller.gmsubscribe.data.repository.GMSubscribeCartRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.repository.GMSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GMSubscribeCartCloud;
import com.tokopedia.seller.gmsubscribe.data.source.product.GMSubscribeProductListSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.cache.GMSubscribeProductCache;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.GMSubscribeProductCloud;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckGMSubscribeVoucherUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGMSubscribeUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGMSubscribeWithVoucherCheckUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetAutoSubscribeSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetCurrentSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.view.checkout.presenter.GMCheckoutPresenterImpl;
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
public class GMCheckoutDependencyInjection {
    public static GMCheckoutPresenterImpl createPresenter() {
        Gson gson = new Gson();

        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();


        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        GMSubscribeProductCache gmSubscribeProductCache = new GMSubscribeProductCache(globalCacheManager);

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
        OkHttpClient client =  clientBuilder.build();

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
        GMSubscribeCartCloud cartCloud = new GMSubscribeCartCloud(cartRetrofit);

        GMSubscribeProductMapper gmSubscribeProductMapper = new GMSubscribeProductMapper();
        GMSubscribeVoucherMapper voucherMapper = new GMSubscribeVoucherMapper();
        GMSubscribeCheckoutMapper checkoutMapper = new GMSubscribeCheckoutMapper();

        GMSubscribeProductListSource gmSubscribeProductListSource = new GMSubscribeProductListSource(gmSubscribeProductCache, gmSubscribeProductCloud, gmSubscribeProductMapper, gson);
        GMSubscribeProductFactory gmSubscribeProductFactory = new GMSubscribeProductFactory(gmSubscribeProductListSource);
        GMSubscribeCartFactory gmSubscribeCartFactory = new GMSubscribeCartFactory(cartCloud, gson, voucherMapper, checkoutMapper);
        GMSubscribeCartRepositoryImpl gmSubscribeCartRepository = new GMSubscribeCartRepositoryImpl(gmSubscribeCartFactory);
        GMSubscribeProductRepositoryImpl gmSubscribeProductRepository = new GMSubscribeProductRepositoryImpl(gmSubscribeProductFactory);

        GetCurrentSelectedProductUseCase getCurrentSelectedProduct = new GetCurrentSelectedProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        GetAutoSubscribeSelectedProductUseCase getAutoSubscribeSelectedProductUseCase = new GetAutoSubscribeSelectedProductUseCase(threadExecutor, postExecutionThread, gmSubscribeProductRepository);
        CheckGMSubscribeVoucherUseCase checkGMSubscribeVoucherUseCase = new CheckGMSubscribeVoucherUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);
        CheckoutGMSubscribeUseCase checkoutGMSubscribeUseCase = new CheckoutGMSubscribeUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);
        CheckoutGMSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase = new CheckoutGMSubscribeWithVoucherCheckUseCase(threadExecutor, postExecutionThread, gmSubscribeCartRepository);

        return new GMCheckoutPresenterImpl(getCurrentSelectedProduct, getAutoSubscribeSelectedProductUseCase, checkGMSubscribeVoucherUseCase, checkoutGMSubscribeUseCase, checkoutGMSubscribeWithVoucherCheckUseCase);
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
