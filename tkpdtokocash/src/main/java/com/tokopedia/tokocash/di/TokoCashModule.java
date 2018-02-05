package com.tokopedia.tokocash.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.storage.GlobalCacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.activation.data.ActivateRepository;
import com.tokopedia.tokocash.activation.domain.LinkedTokoCashUseCase;
import com.tokopedia.tokocash.activation.domain.RequestOtpTokoCashUseCase;
import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.domain.GetReasonHelpDataUseCase;
import com.tokopedia.tokocash.network.WalletUserSession;
import com.tokopedia.tokocash.network.WalletTokenRefresh;
import com.tokopedia.tokocash.network.api.TokoCashApi;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.tokocash.network.api.WalletUrl;
import com.tokopedia.tokocash.network.interceptor.TokoCashAuthInterceptor;
import com.tokopedia.tokocash.network.interceptor.WalletAuthInterceptor;
import com.tokopedia.tokocash.network.interceptor.WalletErrorResponseInterceptor;
import com.tokopedia.tokocash.network.model.TokoCashErrorResponse;
import com.tokopedia.tokocash.network.model.WalletErrorResponse;
import com.tokopedia.tokocash.qrpayment.data.repository.QrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.data.repository.TokoCashBalanceRepository;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.PostQrPaymentUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nabillasabbaha on 12/27/17.
 */
@Module
public class TokoCashModule {

    public TokoCashModule() {

    }

    @Provides
    @TokoCashScope
    WalletUserSession provideTokoCashSession(@ApplicationContext Context context) {
        if (context instanceof TokoCashRouter) {
            return ((TokoCashRouter) context).getTokoCashSession();
        }
        return null;
    }

    @Provides
    @TokoCashScope
    GlobalCacheManager provideGlobalCacheManager(@ApplicationContext Context context) {
        if (context instanceof TokoCashRouter) {
            return ((TokoCashRouter) context).getGlobalCacheManager();
        }
        return null;
    }

    @Provides
    @TokoCashScope
    @OkHttpTokoCashQualifier
    OkHttpClient provideOkHttpClient(TokoCashAuthInterceptor tokoCashAuthInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(tokoCashAuthInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(TokoCashErrorResponse.class))
                .build();
    }

    @Provides
    @TokoCashScope
    @RetrofitTokoCashQualifier
    Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder, @OkHttpTokoCashQualifier OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(WalletUrl.ACCOUNTS_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @TokoCashScope
    TokoCashApi provideTokoCashApi(@RetrofitTokoCashQualifier Retrofit retrofit) {
        return retrofit.create(TokoCashApi.class);
    }

    @Provides
    @TokoCashScope
    WalletTokenRefresh provideWalletTokenRefresh(WalletUserSession walletUserSession, @RetrofitTokoCashQualifier Retrofit retrofit) {
        return new WalletTokenRefresh(walletUserSession, retrofit);
    }

    @Provides
    @TokoCashScope
    @OkHttpWalletQualifier
    OkHttpClient provideOkHttpClientWallet(WalletAuthInterceptor walletAuthInterceptor, AbstractionRouter abstractionRouter,
                                           WalletTokenRefresh walletTokenRefresh, WalletUserSession walletUserSession, Gson gson) {
        return new OkHttpClient.Builder()
                .addInterceptor(walletAuthInterceptor)
                .addInterceptor(new WalletErrorResponseInterceptor(WalletErrorResponse.class, abstractionRouter, walletTokenRefresh, walletUserSession, gson))
                .build();
    }

    @Provides
    @TokoCashScope
    @RetrofitWalletQualifier
    Retrofit provideRetrofitWallet(Retrofit.Builder retrofitBuilder,
                                   @OkHttpWalletQualifier OkHttpClient okHttpClient, Gson gson) {
        return retrofitBuilder.baseUrl(WalletUrl.WALLET_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @TokoCashScope
    WalletApi provideWalletApi(@RetrofitWalletQualifier Retrofit retrofit) {
        return retrofit.create(WalletApi.class);
    }

    @Provides
    @TokoCashScope
    GetHistoryDataUseCase provideGetHistoryDataUseCase(WalletRepository walletRepository) {
        return new GetHistoryDataUseCase(walletRepository);
    }

    @Provides
    @TokoCashScope
    GetReasonHelpDataUseCase provideGetReasonHelpDataUseCase(WalletRepository walletRepository) {
        return new GetReasonHelpDataUseCase(walletRepository);
    }

    @Provides
    @TokoCashScope
    GetInfoQrTokoCashUseCase provideGetInfoQrTokoCashUseCase(QrPaymentRepository qrPaymentRepository) {
        return new GetInfoQrTokoCashUseCase(qrPaymentRepository);
    }

    @Provides
    @TokoCashScope
    PostQrPaymentUseCase providePostQrPaymentUseCase(QrPaymentRepository qrPaymentRepository) {
        return new PostQrPaymentUseCase(qrPaymentRepository);
    }

    @Provides
    @TokoCashScope
    GetBalanceTokoCashUseCase provideGetBalanceTokoCashUseCase(TokoCashBalanceRepository tokoCashBalanceRepository) {
        return new GetBalanceTokoCashUseCase(tokoCashBalanceRepository);
    }

    @Provides
    @TokoCashScope
    LinkedTokoCashUseCase provideActivateTokoCashUseCase(ActivateRepository activateRepository) {
        return new LinkedTokoCashUseCase(activateRepository);
    }

    @Provides
    @TokoCashScope
    RequestOtpTokoCashUseCase provideRequestOtpTokoCashUseCase(ActivateRepository activateRepository) {
        return new RequestOtpTokoCashUseCase(activateRepository);
    }
}
