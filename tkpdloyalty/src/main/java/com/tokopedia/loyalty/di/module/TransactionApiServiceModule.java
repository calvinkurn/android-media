package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.loyalty.di.qualifier.LoyaltyCartApiInterceptorQualifier;
import com.tokopedia.loyalty.di.qualifier.LoyaltyCartApiOkHttpClientQualifier;
import com.tokopedia.loyalty.di.qualifier.LoyaltyCartApiRetrofitQualifier;
import com.tokopedia.loyalty.di.qualifier.LoyaltyCartChuckApiInterceptorQualifier;
import com.tokopedia.loyalty.di.qualifier.LoyaltyCartFingerPrintApiInterceptorQualifier;
import com.tokopedia.loyalty.di.qualifier.LoyaltyCartOkHttpRetryQualifier;
import com.tokopedia.loyalty.router.ITkpdLoyaltyModuleRouter;
import com.tokopedia.transactiondata.apiservice.CartApi;
import com.tokopedia.transactiondata.apiservice.CartApiInterceptor;
import com.tokopedia.transactiondata.apiservice.CartResponseConverter;
import com.tokopedia.transactiondata.constant.TransactionDataApiUrl;
import com.tokopedia.transactiondata.repository.CartRepository;
import com.tokopedia.transactiondata.repository.ICartRepository;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anggaprasetiyo on 07/06/18.
 */
@Module
public class TransactionApiServiceModule {

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 0;

    @Provides
    @LoyaltyCartOkHttpRetryQualifier
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );
    }

    @Provides
    @LoyaltyCartChuckApiInterceptorQualifier
    ChuckInterceptor provideChuckInterceptor(ITkpdLoyaltyModuleRouter loyaltyModuleRouter) {
        return loyaltyModuleRouter.loyaltyModuleRouterGetCartCheckoutChuckInterceptor();
    }

    @Provides
    @LoyaltyCartFingerPrintApiInterceptorQualifier
    FingerprintInterceptor fingerprintInterceptor(ITkpdLoyaltyModuleRouter loyaltyModuleRouter) {
        return loyaltyModuleRouter.loyaltyModuleRouterGetCartCheckoutFingerPrintInterceptor();
    }

    @Provides
    @LoyaltyCartApiInterceptorQualifier
    CartApiInterceptor getCartApiInterceptor(@ApplicationContext Context context,
                                             UserSession userSession,
                                             AbstractionRouter abstractionRouter) {
        return new CartApiInterceptor(
                context, abstractionRouter, userSession, TransactionDataApiUrl.Cart.HMAC_KEY
        );
    }

    @Provides
    @LoyaltyCartApiOkHttpClientQualifier
    OkHttpClient provideCartApiOkHttpClient(
            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
            @LoyaltyCartApiInterceptorQualifier CartApiInterceptor cartApiInterceptor,
            @LoyaltyCartOkHttpRetryQualifier OkHttpRetryPolicy okHttpRetryPolicy,
            @LoyaltyCartFingerPrintApiInterceptorQualifier FingerprintInterceptor fingerprintInterceptor,
            @LoyaltyCartChuckApiInterceptorQualifier ChuckInterceptor chuckInterceptor
    ) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(cartApiInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor);
        }
        return builder.build();
    }

    @Provides
    @LoyaltyCartApiRetrofitQualifier
    Retrofit provideCartApiRetrofit(
            ITkpdLoyaltyModuleRouter loyaltyModuleRouter,
            @LoyaltyCartApiOkHttpClientQualifier OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(TransactionDataApiUrl.Cart.BASE_URL)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(loyaltyModuleRouter.loyaltyModuleRouterGetStringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    @Provides
    CartApi provideCartApi(@LoyaltyCartApiRetrofitQualifier Retrofit retrofit) {
        return retrofit.create(CartApi.class);
    }

    @Provides
    ICartRepository provideICartRepository(CartApi cartApi) {
        return new CartRepository(cartApi);
    }

}
